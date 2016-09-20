package org.tools.hqlbuilder.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jhaws.common.jaxb.adapters.ClassAdapter;

@XmlRootElement
public class HibernateWebResolver implements Serializable {
    private static final long serialVersionUID = 756239472693625438L;

    protected Map<String, ClassNode> nodes = new HashMap<>();

    public HibernateWebResolver() {
        super();
    }

    public List<ClassNode> getClasses() {
        return new ArrayList<>(nodes.values());
    }

    public List<Property> getProperties(String classname) {
        return nodes.get(classname).getEdges();
    }

    public ClassNode getNode(String classname) {
        return nodes.get(classname);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return nodes.toString();
    }

    public Map<String, ClassNode> getNodes() {
        return this.nodes;
    }

    public void setNodes(Map<String, ClassNode> nodes) {
        this.nodes = nodes;
    }

    @XmlRootElement
    public static class ClassNode extends Node<String, String, Property> {
        private static final long serialVersionUID = 3105646728435394121L;

        @XmlJavaTypeAdapter(ClassAdapter.class)
        protected Class<?> type;

        public ClassNode() {
            super();
        }

        public ClassNode(String id) {
            super(id);

            try {
                type = Class.forName(id);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException(id, ex);
            }
        }

        @Override
        protected Property createEdge(String eid) {
            return new Property(eid);
        }

        public Class<?> getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return type.toString();
        }

        public void setType(Class<?> type) {
            this.type = type;
        }
    }

    @XmlRootElement
    public static class Property extends Edge<String, String, Property> {
        private static final long serialVersionUID = -7935969067739133609L;

        @XmlAttribute
        protected boolean collection;

        public Property() {
            super();
        }

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

    public ClassNode getOrCreateNode(String className) {
        ClassNode node = nodes.get(className);
        if (node == null) {
            node = new ClassNode(className);
            nodes.put(className, node);
        }
        return node;
    }

    @XmlRootElement
    public static class Edge<NodeId, EdgeId, EdgeImpl extends Edge<NodeId, EdgeId, EdgeImpl>> implements Serializable {
        private static final long serialVersionUID = 8936945680611529186L;

        protected EdgeId id;

        protected Node<NodeId, EdgeId, EdgeImpl> target;

        public Edge() {
            super();
        }

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

        public void setId(EdgeId id) {
            this.id = id;
        }

        public void setTarget(Node<NodeId, EdgeId, EdgeImpl> target) {
            this.target = target;
        }
    }

    @XmlRootElement
    public static abstract class Node<NodeId, EdgeId, EdgeImpl extends Edge<NodeId, EdgeId, EdgeImpl>> implements Serializable {
        private static final long serialVersionUID = -422807599223330528L;

        protected NodeId id;

        protected List<EdgeImpl> edges = new ArrayList<>();

        public Node() {
            super();
        }

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

        public void setId(NodeId id) {
            this.id = id;
        }

        public void setEdges(List<EdgeImpl> edges) {
            this.edges = edges;
        }
    }
}
