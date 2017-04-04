package org.tools.hqlbuilder.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.common.HibernateWebResolver.ClassNode;
import org.tools.hqlbuilder.common.HibernateWebResolver.Property;

/**
 * @author Jurgen
 */
public class PathResolver {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PathResolver.class);

    final ArrayList<TreeNode> all = new ArrayList<>();

    TreeNode sourceTreeNode;

    TreeNode targetTreeNode;

    final ArrayList<TreeNode> todosCurrentStep = new ArrayList<>();

    final ArrayList<TreeNode> todosFutureStep = new ArrayList<>();

    public PathResolver(ClassNode sourceNode, ClassNode targetNode) {
        sourceTreeNode = new TreeNode(sourceNode);
        targetTreeNode = new TreeNode(targetNode);
        all.add(sourceTreeNode);
        todosCurrentStep.add(sourceTreeNode);
    }

    @SuppressWarnings("unchecked")
    public List<TreeNode> findPathNoEx() {
        try {
            findPath();
        } catch (Result ex) {
            return ex.getResolvePath();
        } catch (End ex) {
            return Collections.emptyList();
        }
        throw new IllegalArgumentException();
    }

    public void findPath() throws Result, End {
        while (true) {
            for (TreeNode todoCurrentStep : todosCurrentStep.toArray(new TreeNode[0])) {
                todosCurrentStep.remove(todoCurrentStep);
                List<TreeNode> findPath = findPath(todoCurrentStep);
                todosFutureStep.addAll(findPath);
            }
            if (todosFutureStep.isEmpty()) {
                throw new End();
            }
            todosCurrentStep.addAll(todosFutureStep);
            todosFutureStep.clear();
        }
    }

    private List<TreeNode> findPath(TreeNode sourceNode) throws Result {
        return sourceNode.getChildren(all, targetTreeNode);
    }

    public static class TreeNode {
        ClassNode value;

        TreeNode parent;

        Property property;

        List<TreeNode> children = new ArrayList<>();

        boolean init;

        public TreeNode(ClassNode value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.value = value;
        }

        public TreeNode(Property property, List<TreeNode> exclude, TreeNode targetNode, TreeNode parent, ClassNode value) throws Result, Exclude {
            if (value == null) {
                throw new NullPointerException();
            }
            this.value = value;
            this.parent = parent;
            this.property = property;

            if (this.value.equals(targetNode.value)) {
                throw new Result(this.resolvePath());
            }

            if (parent != null) {
                if (exclude.contains(this)) {
                    throw new Exclude("circular reference");
                }

                parent.children.add(this);
            }
        }

        public List<TreeNode> resolvePath() {
            List<TreeNode> path = new ArrayList<>();
            TreeNode current = this;
            path.add(current);
            while (current.parent != null) {
                current = current.parent;
                path.add(current);
            }
            Collections.reverse(path);
            return path;
        }

        public List<TreeNode> getChildren(List<TreeNode> exclude, TreeNode targetNode) throws Result {
            if (!init) {
                for (Property sub : value.getEdges()) {
                    try {
                        logger.debug("{}", sub);
                        TreeNode c = new TreeNode(sub, exclude, targetNode, this, ClassNode.class.cast(sub.getTarget()));
                        logger.debug(String.valueOf(c));
                        exclude.add(c);
                    } catch (Exclude ex) {
                        //
                    }
                }
                init = true;
            }
            return children;
        }

        @Override
        public String toString() {
            if (parent != null) {
                return parent.toString() + " > " + value.toString();
            }
            return value.toString();
        }

        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof TreeNode)) {
                return false;
            }
            TreeNode castOther = (TreeNode) other;
            return new EqualsBuilder().append(value, castOther.value).append(property, castOther.property).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(value).append(property).toHashCode();
        }
    }

    public static class Result extends Exception {
        private static final long serialVersionUID = -1425028686878185065L;

        List<TreeNode> resolvePath;

        public Result(List<TreeNode> resolvePath) {
            this.resolvePath = resolvePath;
        }

        public List<TreeNode> getResolvePath() {
            return this.resolvePath;
        }
    }

    public static class Exclude extends Exception {
        private static final long serialVersionUID = 3115494398438245176L;

        public Exclude(String message) {
            super(message);
        }
    }

    public static class End extends Exception {
        private static final long serialVersionUID = -8081495878268907791L;
    }
}
