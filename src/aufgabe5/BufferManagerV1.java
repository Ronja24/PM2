package aufgabe5;


/*
 *------------------------------------------------------------------------------
 * VCS: git@BitBucket.org:schaefers/Prg_Px_K-L_FixedSizeThreadSafeBufferManager_Distr[.git]
 * For further information see ReadMe.txt
 *                                                Michael Schaefers  2017/06/06
 *------------------------------------------------------------------------------
 */
public class BufferManagerV1<T> implements BufferManager<T> {
    final int capacity;
   final T[] theBuffer;
   int nod;
   int vp =0;
    
    public BufferManagerV1( final int capacity ){
        this.capacity =capacity;
        theBuffer = (T[])( new Object[capacity] );
        nod=0;
    }//constructor()
    
    
    
    @Override
    public synchronized void  insert( final T data ) throws InterruptedException {
        try {
        while (nod >= capacity){ wait(); }//warten bis Platz frei da
        theBuffer[nod] = data; //ins Lager tun
    
        // ep = (ep + 1) % capacity; //Verwaltungskram
        nod++; // aktualisieren
        draw();
        notifyAll(); //"bescheid sagen"
    }
        catch(InterruptedException ex){ }
    }//method()
    
    @Override
    public synchronized T remove() throws InterruptedException {
       T resu = null;
        try {
            while (nod <= 0){ wait(); } //warten bis Produkt da
            resu = theBuffer[vp]; //aus dem Lager holen
            theBuffer[vp] = null; //nicht nötig, aber sicherer
           // vp = (vp + 1) % capacity; //Verwaltungskram
            nod--; // aktualisieren
            sort();
            draw();
            notifyAll(); //"bescheid sagen"
            }catch ( InterruptedException ex ){ ex.printStackTrace(); }
                return resu;
        
    }//method()
    public void sort(){
       
    for(int i =0; i <capacity-1; i++){
    theBuffer[i]=theBuffer[i+1];
    }
     theBuffer[capacity-1]=null;
    }
    @Override
    public int getUsage() {
        // TODO
        return nod;
    }//method()
    
    @Override
    public int getRemainingCapacity() {
        int remainingCapacity = capacity -nod;
        return remainingCapacity;
    }//method()
    
    @Override
    public int getCapacity() {
        
        return capacity;
    }//method()
    public void draw(){
        System.out.println("Lagerstand:");
    for(int i =0; i <capacity; i++){
    System.out.print("  "+theBuffer[i]);
    }
    System.out.println();
    System.out.println("Verbrauchter Platz: "+getUsage() );
    System.out.println("Noch freier Platz: "+getRemainingCapacity());
    
    }
}//class
