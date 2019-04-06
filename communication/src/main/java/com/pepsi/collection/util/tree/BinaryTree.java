package com.pepsi.collection.util.tree;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/03/01
 * describe:
 */
public class BinaryTree{

    private Node root;

    public class  Node{

        private int data;
        private Node parent;
        private Node left;
        private Node right;

        public Node(int data,Node parent){
            this.data = data;
            this.parent = parent;

        }

        public Node(int data,Node parent,Node left,Node right){
            this.data = data;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }


    /***
     * 查找一个节点信息
     * @param key
     * @return
     */
    public Node find(int key) {
        Node t = root;
        while (t!=null){
            if(key > t.data){
                t = t.right;
            }else if(key<t.data) {
                t = t.left;
            }else{
                return t;
            }
        }
        return null;
    }

    /****
     * 插入一个节点。
     * @param key
     * @return
     */
    public int insert(int  key) {
        Node t = root;
        if(t!=null){
            return insert(t,key);
        }
        return 0;
    }


    public int insert(Node node,int  key) {

        Node t = root;
        Node p = t;
        while(t != null){
            p  = t;
            if(key > t.data){
                t = t.right;
            }
            if(key < t.data){
                t = t.left;
            }
        }
        Node newNode = new Node(key,p);

        if(p.data>key){
            p.right = newNode;
        }
        if(p.data<key ){
            p.left = newNode;
        }
        return key;
    }


    /***
     * 中序排序
     *
     */
    public void sort(Node node){
        if(node!=null){
            sort(node.left);
            System.out.println(node.data);
            sort(node.right);
        }
    }

    /***
     * 删除元素如果细分的话，可以分为4中：没有子节点，只有左节点，只有右节点，有两个子节点
     * 第三个情况： 前置节点或者后驱节点顶上来
     *  前置节点
            删除节点的左子节点 没有右子节点，
            删除节点的左叶子节点有叶子节点，那么最右边最后一个节点。
     *  后驱节点
     *      和前置节点相反。
     *      删除节点的右子节点的最左最后的一个节点。
     *
     *
     *
     * @param key
     * @return
     */
    public boolean delete(Object key) {



        return false;
    }


    public static void main(String[] args) {
        int index  = -99;
        for(int i = 31;i >= 0; i--){
            System.out.print(index >>> i & 1);
        }
        System.out.println("-----------------"+Integer.toBinaryString(100));
    }

}
