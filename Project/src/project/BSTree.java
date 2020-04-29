package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Jasen Ratnam 
 * 40094237
 * COEN 352, final project
 * Due date: December 2nd 2019
 * This class defines the BST with string data
 */
public class BSTree 
{
    private BST_NODE root; // pointer to the root of the BST
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
     *  construct the BSt with a null root
     */
    public BSTree() 
    {
        root = null; 
    } 

    /**
     * insert a element to the BST
     * No duplicates allowed
     * If duplicate; do nothing
     * @param key element to be added
     */
    public void insert(String key) 
    {
        root = insert(root, key);
    }
   
    /**
     * insert an element to a specific node
     * @param n node in which element is added
     * @param k element to be added
     * @return the node with element added
     */
    private BST_NODE insert(BST_NODE n, String k) 
    {
        // if node is empty; fill it
        if (n == null) 
        {
            return new BST_NODE(k, null, null);
        }
        
        // if duplicate
        if (n.getKey().equals(k)) 
        {
            // duplicate do nothing, do not add it
        }
        // if element is smaller then current element
        if (k.compareTo(n.getKey()) < 0) 
        {
            // add k to the left subtree
            n.setLeft(insert(n.getLeft(), k));
            return n;
        }
        // if element is bigger then current element
        else 
        {
            // add k to the right subtree
            n.setRight(insert(n.getRight(), k));
            return n;
        }
    }
    
    /**
     *  search for an element in the BST
     * @param key element to be searched
     * @return  true if found, false if not found
     */
    public boolean lookup(String key) 
    {
        // first try in root
        return lookup(root, key);
    }

    /**
     * search for element in current node
     * @param n node to be checked
     * @param k element to be found
     * @return true if found, false if found
     */
    private boolean lookup(BST_NODE n, String k) 
    {
        //node is empty
        if (n == null) return false;
        // if node is the element
        if (n.getKey().equals(k)) return true;
        
        // if element is smaller then current element
        if (k.compareTo(n.getKey()) < 0) 
        {
            // k < this node's key; look in left subtree
            return lookup(n.getLeft(), k);
        }
        
        // if element is bigger then current element
        else 
        {
            // k > this node's key; look in right subtree
            return lookup(n.getRight(), k);
        }
    }
      
     /**
     * Method that creates a BST from a dictionary file input
     * @param dictionaryFile  Input path to the dictionary file
     * @return a BST populated from the dictionary text file given
     */
    public static BSTree createTree(String dictionaryFile) 
    {
        File f = new File(dictionaryFile);
        String filename;
        while(!f.exists())
        {
            System.out.println("ERROR: Please enter the name of dictionary text file: ");
            Scanner keyboard = new Scanner(System.in);
            filename = keyboard.nextLine();
            dictionaryFile = filename + ".txt";
            f = new File(dictionaryFile);
        }
        BSTree bstree = new BSTree();
        Scanner sc2 = null;
        try 
        {
            if(DEBUG)
            {
                System.out.println("The words from dictionary text file being put in BST");
            }
            sc2 = new Scanner(new File(dictionaryFile));
            while (sc2.hasNextLine()) 
            {
                Scanner s2 = new Scanner(sc2.nextLine());

                while (s2.hasNext()) 
                {
                    String s = s2.next();
                    String s_low = s.toLowerCase();
                    if(DEBUG)
                    {
                        System.out.println(s);
                    }
                    bstree.insert(s_low);
                }
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();  
        }
        
        return bstree;
    }
    
    /**
     * Method to traverse the BST in pre order.
     */
    public void traversePreOrder() 
    {   
        if(DEBUG)
        {
            System.out.println(this.root.getKey());
        }
        
        // go to left branch
        if (this.root.getLeft() != null)
        {
            this.root.getLeft().traversePreOrder();
        }
        
        // go to rigth branch
        if (this.root.getRight() != null) 
        {
            this.root.getRight().traversePreOrder();
        }
    }
}
