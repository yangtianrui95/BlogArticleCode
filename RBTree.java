/**
 * 红黑树基础实现/递归版本/仅做学习用
 *
 * @author yangtianrui
 */
public class RBTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public static void main(String[] args) {
        RBTree rbTree = new RBTree();

        rbTree.put(1);
        rbTree.put(2);
        rbTree.put(3);
        rbTree.put(4);
        rbTree.put(5);
        rbTree.put(6);
        rbTree.put(7);
        rbTree.put(8);
        rbTree.put(9);

    }


    static class Node {


        int key;
        boolean color;
        Node left;
        Node right;

        public Node(int key, boolean color) {
            this.key = key;
            this.color = color;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }


    private Node mRoot;

    public RBTree() {

    }

    /**
     * 左旋
     *
     * @param pivot 基准点
     * @return 旋转后的基准点
     */
    private static Node rotateLeft(Node pivot) {
        Node newPivot = pivot.right;

        pivot.right = newPivot.left;
        newPivot.left = pivot;

        newPivot.color = pivot.color;
        pivot.color = RED;
        return newPivot;
    }

    /**
     * 右旋
     *
     * @param pivot 基准点
     * @return 旋转后的基准点
     */
    private static Node rotateRight(Node pivot) {
        Node newPivot = pivot.left;

        newPivot.left = pivot.right;
        pivot.right = newPivot;

        newPivot.color = pivot.color;
        pivot.color = RED;

        return newPivot;
    }

    /**
     * 变色
     *
     * @param pivot
     * @return
     */
    private static Node flipColor(Node pivot) {
        pivot.color = RED;
        pivot.left.color = BLACK;
        pivot.right.color = BLACK;
        return pivot;
    }


    public void put(int key) {
        if (mRoot == null) {
            mRoot = new Node(key, BLACK);
            return;
        }
        mRoot = put(mRoot, key);
        mRoot.color = BLACK;
    }

    /**
     * 判断该节点是否是红节点
     *
     * @param node 需要判断的节点
     * @return true/false 非红即黑
     */
    private static boolean isRed(Node node) {
        return node != null && node.color == RED;
    }


    /**
     * 查找操作/与BST相同
     *
     * @param key 要查找的key
     * @return node 节点
     */
    public Node search(Node start, int key) {
        if (start == null) {
            return null;
        }
        if (key == start.key) {
            return start;
        }
        if (key > start.key) {
            return search(start.right, key);
        } else {
            return search(start.left, key);
        }
    }

    /**
     * 向指定Node中插入节点
     *
     * @param node 从哪个节点开始插入
     * @param key  要插入的key
     * @return 该节点插入的位置
     */
    private Node put(Node node, int key) {
        if (node == null) {
            return new Node(key, RED);
        }
        if (key == node.key) {
            node.key = key;
        }
        boolean cmp = key < node.key;
        if (cmp) {
            node.left = put(node.left, key);
        } else {
            node.right = put(node.right, key);
        }
        // 根据红黑树规则修复
        node = fixupAfterPut(node);
        return node;
    }


    private Node fixupAfterPut(Node node) {
        /*
            对插入后的树进行修复
        */

        // 不需要处理的情况： 当前节点为黑色，新元素插入到左子节点。

        // 需要处理的情况，按照如下规则:
        // 规则一：如果出现红色右子节点，黑色左子节点
        if (isRed(node.right) && !isRed(node.left)) {
            // 相对父节点进行旋转
            node = rotateLeft(node);
        }

        // 规则二： 如果当前子节点和孙子节点是红色，那么以子节点为基准右旋并且变色
        if (node.left != null && isRed(node.left) && isRed(node.left.left)) {
            rotateRight(node.left);
        }

        // 规则三：如果出现该节点同时红色左子节点和右子节点，那么进行变色，将左右子节点变黑，当前节点变红。
        if (isRed(node.left) && isRed(node.right)) {
            flipColor(node);
        }
        return node;
    }
}
