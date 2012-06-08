package be.ugent.oasis.tools.hqlbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;

public class HibernateWebResolver {
    protected Map<String, ClassNode> nodes = new HashMap<String, ClassNode>();

    protected final IHqlBuilderHelper hqlBuilderHelper;

    public HibernateWebResolver(IHqlBuilderHelper helper, SessionFactory sessionFactory) {
        this.hqlBuilderHelper = helper;
        for (Object o : sessionFactory.getAllClassMetadata().entrySet()) {
            Entry<?, ?> entry = (Entry<?, ?>) o;
            String className = (String) entry.getKey();
            try {
                ClassNode node = getOrCreateNode(className);
                AbstractEntityPersister abstractEntityPersister = (AbstractEntityPersister) entry.getValue();
                String superclass = abstractEntityPersister.getEntityMetamodel().getSuperclass();
                if (superclass != null) {
                    getOrCreateNode(superclass).addPath("SUB(" + className + ")", node);
                }
                for (String propertyNames : abstractEntityPersister.getPropertyNames()) {
                    Type propertyType = abstractEntityPersister.getPropertyType(propertyNames);
                    if (propertyType instanceof OneToOneType || propertyType instanceof ManyToOneType) {
                        String subClassName = propertyType.getName();
                        node.addPath(propertyNames, getOrCreateNode(subClassName));
                    } else if (propertyType instanceof CollectionType) {
                        CollectionType collectionType = CollectionType.class.cast(propertyType);
                        String subClassName = collectionType.getElementType((SessionFactoryImplementor) sessionFactory).getName();
                        if ("org.hibernate.type.EnumType".equals(subClassName)) {
                            continue;
                        }
                        node.addPath(propertyNames, getOrCreateNode(subClassName)).setCollection(true);
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.err.println(className);
            }
        }
    }

    public List<ClassNode> getClasses() {
        return new ArrayList<ClassNode>(nodes.values());
    }

    public List<Property> getProperties(String classname) {
        return nodes.get(classname).getEdges();
    }

    public class ClassNode extends Node<String, String, Property> {
        protected Class<?> type;

        public ClassNode(String id) {
            super(id);

            try {
                type = Class.forName(id);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException(id, ex);
            }

            if (!hqlBuilderHelper.accept(type)) {
                throw new IllegalArgumentException(id);
            }
        }

        @Override
        protected Property createEdge(String eid) {
            return new Property(eid);
        }

        public Class<?> getType() {
            return this.type;
        }
    }

    public class Property extends Edge<String, String, Property> {
        protected boolean collection;

        public Property(String id) {
            super(id);
        }

        public boolean isCollection() {
            return this.collection;
        }

        public void setCollection(boolean collection) {
            this.collection = collection;
        }
    }

    protected ClassNode getOrCreateNode(String className) {
        ClassNode node = nodes.get(className);
        if (node == null) {
            node = new ClassNode(className);
            nodes.put(className, node);
        }
        return node;
    }

    public static class Edge<NodeId, EdgeId, EdgeImpl extends Edge<NodeId, EdgeId, EdgeImpl>> {
        protected final EdgeId id;

        protected Node<NodeId, EdgeId, EdgeImpl> target;

        public Edge(EdgeId id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
            result = prime * result + ((this.target == null) ? 0 : this.target.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("rawtypes")
            Edge other = (Edge) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }
            if (this.target == null) {
                if (other.target != null) {
                    return false;
                }
            } else if (!this.target.equals(other.target)) {
                return false;
            }
            return true;
        }

        public EdgeId getId() {
            return this.id;
        }

        @Override
        public String toString() {
            return id.toString();
        }

        public Node<NodeId, EdgeId, EdgeImpl> getTarget() {
            return this.target;
        }
    }

    public static abstract class Node<NodeId, EdgeId, EdgeImpl extends Edge<NodeId, EdgeId, EdgeImpl>> {
        protected final NodeId id;

        protected final List<EdgeImpl> edges = new ArrayList<EdgeImpl>();

        public Node(NodeId id) {
            this.id = id;
        }

        protected abstract EdgeImpl createEdge(EdgeId eid);

        public EdgeImpl addPath(EdgeId eid, Node<NodeId, EdgeId, EdgeImpl> target) {
            EdgeImpl edge = createEdge(eid);
            edges.add(edge);
            edge.target = target;
            return edge;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("rawtypes")
            Node other = (Node) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }
            return true;
        }

        public List<EdgeImpl> getEdges() {
            return Collections.unmodifiableList(this.edges);
        }

        public NodeId getId() {
            return this.id;
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
