package org.tools.hqlbuilder.client.demo;

import java.util.Locale;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.swingeasy.UIUtils;
import org.tools.hqlbuilder.client.HqlBuilderFrame;
import org.tools.hqlbuilder.client.HqlServiceClientImpl;
import org.tools.hqlbuilder.client.HqlServiceClientLoaderBean;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.demo.Authority;
import org.tools.hqlbuilder.demo.Group;
import org.tools.hqlbuilder.demo.GroupAuthority;
import org.tools.hqlbuilder.demo.Lang;
import org.tools.hqlbuilder.demo.Member;
import org.tools.hqlbuilder.demo.User;

public class HqlBuilderWebClientDemo {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilderWebClientDemo.class);

    public static void main(final String[] args) {
        try {
            new HqlBuilderWebClientDemo(args);
        } catch (org.springframework.remoting.RemoteConnectFailureException ex) {
            ex.printStackTrace(System.out);
            try {
                String msg = ex.getMessage();
                msg = msg.substring(0, msg.indexOf("; nested exception"));
                JOptionPane.showMessageDialog(null, msg);
            } catch (RuntimeException ex2) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public HqlBuilderWebClientDemo(final String[] args) throws Exception {
        init(args);
    }

    protected void init(final String[] args) throws Exception {
        UIUtils.systemLookAndFeel();
        String v = getHibernateVersion();
        logger.info("Hibernate " + v + "x");
        HqlServiceClientImpl hqlServiceClient = getService();
        logger.debug(hqlServiceClient.getConnectionInfo());
        testData(hqlServiceClient);
        randomData(hqlServiceClient);
        HqlBuilderFrame.start(args, new HqlServiceClientLoaderBean(hqlServiceClient));
    }

    protected ConfigurableApplicationContext context;

    protected ConfigurableApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/client/demo/spring-http-webclient-demo-config.xml");
        }
        return context;
    }

    protected HqlServiceClientImpl getService() {
        return get("hqlServiceClient", HqlServiceClientImpl.class);
    }

    protected PasswordEncoder getPasswordEncoder() {
        return get("passwordEncoder", PasswordEncoder.class);
    }

    protected <T> T get(String id, Class<T> type) {
        return type.cast(getContext().getBean(id));
    }

    protected String getHibernateVersion() {
        String v = "3";
        try {
            String hibv = CommonUtils.readManifestVersion("org.hibernate.Hibernate");
            if (hibv == null) {
                hibv = CommonUtils.readMavenVersion("org.hibernate:hibernate");
            }
            if (hibv == null) {
                hibv = CommonUtils.readMavenVersion("org.hibernate:hibernate-core");
            }
            logger.info(hibv);
            if (hibv.startsWith("4")) {
                v = "4";
            }
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
        }
        return v;
    }

    protected void randomData(final HqlServiceClientImpl service) {
        String rnd = UUID.randomUUID().toString();
        Group group = new Group("group_" + rnd);
        group = service.get(Group.class, service.save(group));
        group = (Group) service.execute(new QueryParameters("from Group g left outer join fetch g.members where g.id=" + group.getId())).getResults()
                .getValue().get(0);
        Member member = new Member("member_" + rnd, group);
        member = service.get(Member.class, service.save(member));
    }

    protected void testData(final HqlServiceClientImpl service) {
        if (service.execute(new QueryParameters("from " + User.class.getSimpleName())).getResults().getValue().size() == 0) {
            try {
                Group admins = new Group("admins");
                admins = service.get(Group.class, service.save(admins));
                Member member = new Member("hqladmin", admins);
                member = service.get(Member.class, service.save(member));
                GroupAuthority groupauthority = new GroupAuthority("ROLE_ADMIN", admins);
                groupauthority = service.get(GroupAuthority.class, service.save(groupauthority));
                Authority authority = new Authority("ROLE_ADMIN", member);
                authority = service.get(Authority.class, service.save(authority));
                Lang langEn = new Lang(Locale.getDefault().getLanguage());
                langEn = service.get(Lang.class, service.save(langEn));
                User admin = new User("hqladmin", "hqladmin", "hqladmin@gmail.com");
                admin.setLanguage(langEn);
                admin.setPassword(getPasswordEncoder().encode("hqladmin"));
                admin.setMember(member);
                admin = service.get(User.class, service.save(admin));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }
}