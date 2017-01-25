package shiftplanning;

import updatables.Updatable;
import updatables.UpdatableOnQuadrantChange;
import java.util.ArrayList;

/**
 *
 * @author phusisian
 */
public class DistanceSorter implements UpdatableOnQuadrantChange, Updatable
{
    /*** DistanceSorter has a few issues involving how it sorts. Currently goes by the closest point to the viewer, but there are times where (generally long) shapes can extend farther back than the solid they are drawn over.
     * Additionally, does not sort based on height, so I can't just clump all different ThreeDDrawables into one list and have it work if there are solids on top of solids.
     */
    /*https://www.cs.cmu.edu/~adamchik/15-121/lectures/Sorting%20Algorithms/code/MergeSort.java Used this as source for merge sort.*/
    private Plane boundPlane;
    private ThreeDDrawable[] threeDDrawables;
    public DistanceSorter(Plane boundPlaneIn, ThreeDDrawable[] threeDDrawablesIn)
    {
        boundPlane = boundPlaneIn;
        threeDDrawables = threeDDrawablesIn;
    }

    public void addThreeDDrawable(ThreeDDrawable threeD){   
        threeDDrawables = ArrayHelper.addThreeDDrawable(threeDDrawables, threeD);
        //System.out.println("Drawables size: " + threeDDrawables.length);
    }
    
    public void printDrawablesSortConstants()
    {
        for(ThreeDDrawable threeD : threeDDrawables)
        {
            System.out.println("Constant: " + threeD.getSortDistanceConstant());
        }
    }
    
    public ThreeDDrawable[] getThreeDDrawables(){
        return threeDDrawables;
    }
     
    private void sortByDistance(ThreeDDrawable[] a)
    {
        ThreeDDrawable[] tmp = new ThreeDDrawable[a.length];
        mergeSort(a, tmp,  0,  a.length - 1);
    }
    
    private void mergeSort(ThreeDDrawable[ ] a, ThreeDDrawable [ ] tmp, int left, int right)
    {
        if( left < right )
        {
            int center = (left + right) / 2;
            mergeSort(a, tmp, left, center);
            mergeSort(a, tmp, center + 1, right);
            merge(a, tmp, left, center + 1, right);
        }
    }
    
    private void merge(ThreeDDrawable[ ] a, ThreeDDrawable[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
        {
            if(compareTo(a[left], a[right]) <= 0)//(a[left].compareTo(a[right]) <= 0)
            {
                tmp[k++] = a[left++];
            }else{
                tmp[k++] = a[right++];
            }
        }

        while(left <= leftEnd)    // Copy rest of first half
        {
            tmp[k++] = a[left++];
        }
        while(right <= rightEnd)  // Copy rest of right half
        {
            tmp[k++] = a[right++];
        }

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
        {
            a[rightEnd] = tmp[rightEnd];
        }
    }
    
    private int compareTo(ThreeDDrawable c1, ThreeDDrawable c2)
    {
        if(c1.getSortDistanceConstant() > c2.getSortDistanceConstant())
        {
            return 1;
        }else if(c1.getSortDistanceConstant() == c2.getSortDistanceConstant())
        {
            return 0;
        }
        return -1;
    }
    
    @Override
    public void updateOnQuadrantChange()
    {
        sortByDistance(threeDDrawables);
    }
    

    @Override
    public void update() 
    {
        sortByDistance(threeDDrawables);
    }
}
