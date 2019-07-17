import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2-3树实现/仅供学习使用
 *
 * @author yangtianrui
 */
public class TwoThreeTree {

    public static void main(String[] args) {
        TwoThreeTree tree = new TwoThreeTree();
        tree.put(1);
        tree.put(2);
        tree.put(3);
        tree.put(4);
        tree.put(5);
        tree.put(6);
        tree.put(7);
        tree.put(8);
        tree.put(9);
    }


    /**
     * 2-3树节点，这里把2-节点和3-节点都放在一起了
     */
    private static class Node {

        private Node parent = null;
        // 该节点保存的数据
        private final List<Integer> keys = new ArrayList<>();
        // 子节点
        private final List<Node> children = new ArrayList<>();

        /**
         * 向节点中插入元素，2-3树无法向下增长，需要在当前节点插入后向上分裂
         *
         * @param num
         */
        public void insert(int num) {
            keys.add(num);
            Collections.sort(keys);
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * 需要分裂
         *
         * @return true/false
         */
        public boolean needSplit() {
            return keys.size() > 2;
        }


        @Override
        public String toString() {
            return "Node{" +
                    "keys=" + keys +
                    '}';
        }
    }

    private Node mRoot;

    public TwoThreeTree() {
    }


    /**
     * 向2-3树中插入节点
     *
     * @param key key
     * @return true 成功 false 失败
     */
    public boolean put(int key) {
        if (mRoot == null) {
            mRoot = new Node();
            mRoot.insert(key);
            return true;
        }
        final Node insertNode = findInsertNode(mRoot, key);
        if (insertNode == null) {
            return false;
        }
        insertNode.insert(key);
        if (insertNode.needSplit()) {
            split(insertNode);
        }
        return true;
    }

    /**
     * 当前节点向上分裂，2-3树保持平衡的核心
     *
     * @param pivot 需要分裂的节点
     */
    private void split(Node pivot) {
        if (pivot == null) {
            return;
        }
        Node parent = pivot.parent;
        // 中间键
        int middle = pivot.keys.get(1);
        // 新分裂的节点
        Node n2 = new Node();

        // 开始分裂
        if (pivot.isLeaf()) {
            /*
                此时是叶子节点分裂，初始分裂状态一定是根节点
            */

            // n2 获取右键
            n2.keys.add(pivot.keys.get(2));
            // 原节点删除右键和中键
            pivot.keys.remove(2);
            pivot.keys.remove(1);
        } else {
            /*
                此时是中间节点分裂，这个状态一般是叶子节点分裂完后出现的。
            */

            // n2 获取后两个键，注意添加顺序
            n2.children.add(pivot.children.get(2));
            n2.children.add(pivot.children.get(3));

            // 删除两个孩子
            pivot.children.remove(3);
            pivot.children.remove(2);

            n2.keys.add(pivot.keys.get(2));
            n2.children.get(0).parent = n2;
            n2.children.get(1).parent = n2;

            // 原节点删除右键和中键
            pivot.keys.remove(2);
            pivot.keys.remove(1);
        }

        // 分裂根节点
        if (parent == null) {
            mRoot = new Node();
            mRoot.parent = null;
            mRoot.children.add(pivot);
            mRoot.children.add(n2);

            // root 节点取中间键
            mRoot.keys.add(middle);
            pivot.parent = mRoot;
            n2.parent = mRoot;
        } else {
            // 把当前分类的n2插入到父节点的孩子节点中
            int indexInParent = pivot.parent.children.indexOf(pivot);
            pivot.parent.children.add(indexInParent + 1, n2);
            pivot.parent.insert(middle);
            n2.parent = parent;
            if (parent.needSplit()) {
                split(parent);
            }
        }

    }

    /**
     * 根据当前key查找到应该插入的位置
     *
     * @param start 初始节点
     * @param key   key
     * @return null key已经存在/Node 可以在此插入
     */
    private Node findInsertNode(Node start, int key) {
        if (mRoot == null) {
            return null;
        }
        if (start.keys.contains(key)) {
            return null;
        }
        if (start.isLeaf()) {
            return start;
        }
        int keyCount = start.keys.size();
        if (key < start.keys.get(0)) {
            // 查找左节点
            return findInsertNode(start.children.get(0), key);
        } else if (key > start.keys.get(keyCount - 1)) {
            // 查找右节点
            // 根据2-3树的定义，右节点一定是keyCount
            return findInsertNode(start.children.get(keyCount), key);
        } else {
            // 查找中间节点
            // 这个时候1代表的是2-3树的中间节点
            return findInsertNode(start.children.get(1), key);
        }
    }

}
