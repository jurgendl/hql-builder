package org.tools.hqlbuilder.client;

import java.util.Locale;

import javax.swing.JOptionPane;

import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.swingeasy.UIUtils;
import org.tools.hqlbuilder.common.CommonUtils;
import org.tools.hqlbuilder.common.QueryParameters;
import org.tools.hqlbuilder.common.exceptions.ValidationException;
import org.tools.hqlbuilder.test.Authority;
import org.tools.hqlbuilder.test.Group;
import org.tools.hqlbuilder.test.GroupAuthority;
import org.tools.hqlbuilder.test.Lang;
import org.tools.hqlbuilder.test.Member;
import org.tools.hqlbuilder.test.User;

public class HqlBuilder {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HqlBuilder.class);

    public static void main(final String[] args) {
        new HqlBuilder(args);
    }

    public HqlBuilder(final String[] args) {
        try {
            init(args);
        } catch (org.springframework.remoting.RemoteConnectFailureException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "No connection to host");
        }
    }

    protected void init(final String[] args) {
        UIUtils.systemLookAndFeel();
        String v = getHibernateVersion();
        logger.info("Hibernate " + v + "x");
        HqlServiceClientImpl hqlServiceClient = getService();
        logger.debug(hqlServiceClient.getConnectionInfo());
        testData(hqlServiceClient);
        HqlBuilderFrame.start(args, new HqlServiceClientLoaderBean(hqlServiceClient));
    }

    protected ConfigurableApplicationContext context;

    //
    protected ConfigurableApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("org/tools/hqlbuilder/client/spring-http-client-config.xml");
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

    protected void testData(final HqlServiceClientImpl hqlServiceClient) {
        if (hqlServiceClient.execute(new QueryParameters("from " + User.class.getSimpleName())).getResults().getValue().size() == 0) {
            try {
                Group admins = new Group("admins");
                admins = hqlServiceClient.get(Group.class, hqlServiceClient.save(admins));
                Member member = new Member("hqladmin", admins);
                member = hqlServiceClient.get(Member.class, hqlServiceClient.save(member));
                GroupAuthority groupauthority = new GroupAuthority("ROLE_ADMIN", admins);
                groupauthority = hqlServiceClient.get(GroupAuthority.class, hqlServiceClient.save(groupauthority));
                Authority authority = new Authority("ROLE_ADMIN", member);
                authority = hqlServiceClient.get(Authority.class, hqlServiceClient.save(authority));
                Lang langEn = new Lang(Locale.getDefault().getLanguage());
                langEn = hqlServiceClient.get(Lang.class, hqlServiceClient.save(langEn));
                User admin = new User("hqladmin", "hqladmin", "hqladmin@gmail.com");
                admin.setLanguage(langEn);
                admin.setPassword(getPasswordEncoder().encode("hqladmin"));
                admin.setMember(member);
                admin = hqlServiceClient.get(User.class, hqlServiceClient.save(admin));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
    }
}