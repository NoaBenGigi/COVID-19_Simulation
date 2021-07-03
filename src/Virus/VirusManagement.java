package Virus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirusManagement {
    /**
     * This class will manage the variants and the contagions
     * while using the strategy dp
     */
    private  static Boolean[][] variantsBooleanTable;
    public enum Variants{
        ChineseVariant(new ChineseVariant(),"Chinese Variant"),
        BritishVariant(new BritishVariant(),"British Variant"),
        SouthAfricanVariant(new SouthAfricanVariant(), "SouthAfrican Variant");
        private IVirus virus;
        private String variantName;
        Variants(IVirus virus,String variantName){
            this.virus = virus;
            this.variantName=variantName;
        }
        public Variants[] getVariants(){
            return Variants.values();
        }
        public IVirus getVirus() { return virus; }
        public String getVariantName() { return variantName; }
        public static int getVirusByIndex(IVirus virus) {
            for (int i = 0; i < Variants.values().length; i++) {
                if (Variants.values()[i].virus.isEqual(virus) )
                    return i;
            }
            return -1;
        }
    }
    static { //initialize the table
        variantsBooleanTable = new Boolean[Variants.values().length][];
        for(int i=0; i<variantsBooleanTable.length;i++){
            variantsBooleanTable[i]=new Boolean[Variants.values().length];
            for(int j=0; j<variantsBooleanTable.length;j++)
                if(i!=j)
                    variantsBooleanTable[i][j]=false;
                else
                    variantsBooleanTable[i][j]=true;
        }
    }
    public static void setVariantTable(int i,int j){
        //This func will set the opposite value in variantsBooleanTable[i][j]
        variantsBooleanTable[i][j] = !variantsBooleanTable[i][j];
    }
    public static Boolean[][] getVariantsBooleanTable(){
        return variantsBooleanTable;
    }
    public static IVirus randomVariantToContagion(IVirus virus)
    {
        int index = Variants.getVirusByIndex(virus);
        if(index==-1) {
            return null;
        }
        IVirus otherVirus=findMutation(variantsBooleanTable[index]);
        if(otherVirus == null)
            return virus;
        else
            return otherVirus;
    }

    public static IVirus findMutation(Boolean[] data) {
        int numOfTrueVal=0;
        int[] indexOfTrue=null;
        for(int i=0;i<data.length;i++) {
            if(data[i])
                numOfTrueVal++;
        }
        indexOfTrue=new int[numOfTrueVal];
        int j=0;
        for(int i=0;i<data.length;i++) {
            if(data[i]) {
                indexOfTrue[j]=i;
                j++;
            }
        }
        if(numOfTrueVal==0)
            return null;
        Random randomVirus=new Random();
        int numOfRandomVirus=randomVirus.nextInt(numOfTrueVal);
        return Variants.values()[indexOfTrue[numOfRandomVirus]].getVirus();
    }
}
