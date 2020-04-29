package project;

/**
 * @author Jasen Ratnam 
 * 40094237
 * COEN 352, final project
 * Due date: December 2nd 2019
 * Given a dictionary of distinct words and an input file, find the frequency
 * of words in the text file that exist in the dictionary.
 * This clasee defines a node of the BST
 */
public class BST_NODE 
{
    // element
    private String key;
    // left & rigth nodes
    private BST_NODE left, right;
    // too debug
    private static Boolean DEBUG;

    /**
     *  Gets the DEBUG variable fromm project main class and set the DEBUG in this class
     * @param debug to debug the code
     */
    public static void setDebug(Boolean debug)
    {
        DEBUG = debug;
    }
    
    /**
     * Constructor with element and left and right nodes
     * @param k element of current node
     * @param l pointer to left node
     * @param r pointer to right node
     */
   public BST_NODE(String k, BST_NODE l, BST_NODE r) 
   {
       key = k;
       left = l;
       right = r;
   }

    /**
     * Get element of current node
     * @return element
     */
   public String getKey() 
   {
       return key;
   }
 
    /**
     * get the left node
     * @return left node
     */
    public BST_NODE getLeft() 
   {
       return left;
   }

    /**
     * get the right node
     * @return rigth node
     */
    public BST_NODE getRight()
   {
       return right;
   }
    
    /**
     * change the element of the current node
     * @param k element to be changed too
     */
   public void setKey(String k) 
   {
       key = k;
   }
 
    /**
     * change the left node
     * @param l
     */
    public void setLeft(BST_NODE l) 
   {
       left = l;
   }

    /**
     * change the right node
     * @param r
     */
    public void setRight(BST_NODE r)
   {
       right = r;
   }
   
    /**
     * go trough the tree
     */
    public void traversePreOrder() 
    {  
        if(DEBUG)
        {
            System.out.println(key);
        }

        // if left node exists go to left node
        if (left != null)
        {
            left.traversePreOrder();
        }

        // if right node exists go to right node
        if (right != null) 
        {
            right.traversePreOrder();
        }
    }
}
