import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
 
/**
 * @author Dima Soukhman 317639615
 * Excercise to find best place/places for kitchen with cheapest sum from all employees to kitchen 
 *
 *1) First step loading plan of room floor from the file,path of the file is not fixed,
 *       you can run this project from command line 
 *       with file argument you want. 
 */
public class kitchenPath {

	/**
	 * @param args
	 */
	static int height ;
	static int width  ;

	static int xKitchenIndex ;
	static int yKitchenIndex ;
	static boolean flagCanKithenPLaced = false;
	static int countIndexesPass = 0;
	static int minimalCosting=Integer.MAX_VALUE;
	static int costing ;
	
	static ArrayList<Integer[]> indexesPass = new ArrayList<Integer[]>();	
	static ArrayList<Integer[]> indexesCheckFrom = new ArrayList<Integer[]>();	
    static ArrayList<Integer[]> indexesE = new ArrayList<Integer[]>();
    
    static ArrayList<cellFloor[]> floorMap = new ArrayList<cellFloor[]>();;
    static ArrayList<Integer[]> indexsBestCosting = new ArrayList<Integer[]>();
    
    static boolean canKitchenBePlaced;
    public static void initFloorMap(String file) throws FileNotFoundException {
   	 
    	Scanner input = new Scanner(new File(file));
		int sizeRows = 0;
		int sizeCols;
		String str;
		System.out.println("Original Floor Map Plan:");
    	while(input.hasNextLine())
		{
    		String s=input.nextLine();
		    char[] charArr = s.toCharArray();
		    //System.out.println(s);
		    sizeCols  = charArr.length;
		    floorMap.add(new cellFloor[sizeCols]);
		    
		    int counterChars=0;
		    for (char ch:charArr) {
		    	str=" ";
		    	if(ch=='W') {
		    		str="W";
		    	}
		    	if(ch=='E') {
		    	   str="E";
		    		
		    	}
		    	System.out.print(ch);
		    	floorMap.get(sizeRows)[counterChars]=new cellFloor(str);
		    	counterChars++;
		    }
		    sizeRows++;
		    System.out.println("");
		}
		height = floorMap.size();
		width = floorMap.get(0).length;
		
    }
    public static String loadFile(String[] args) {
    	String filePath="";
    	
    	if (args.length==1) {
    	   filePath = args[0];	
    	}
    	else {
    		String s; 
    		Scanner sc = new Scanner(System.in);
     		System.out.println("Enter File plan path\nOr just Enter to load default path src/files/floorMap.txt"); 
     		s = sc.nextLine();
     		sc.close();
     		if(s.trim().isEmpty()) {
     			//file that loaded in case you don't enter File path 
     			System.out.print("No entered path\nthis default file path is choosen:");
     			filePath = "src/files/floorMap.txt";
     		}
     		else {
     			System.out.print("File path for the plan :");
     			filePath = s;
     		}
    		System.out.println(filePath);
    		
    	}
    	return filePath;
    	
    }
    public static void listsEmployeesEmptiesBuild() {
    	
    	int counterEmptyFields = 0;
		int counterE = 0;
		for (int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				//S=SPACE ,indexes with EMPTY AREA for kitchen position
				if(floorMap.get(i)[j].type == " ") {
					indexesCheckFrom.add(new Integer[2]);
					indexesCheckFrom.get(counterEmptyFields)[0]=i;
					indexesCheckFrom.get(counterEmptyFields)[1]=j;
					counterEmptyFields++;
				}
				//list with all employees to check after that all of them has path to kitchen 
				if(floorMap.get(i)[j].type == "E") {
					indexesE.add(new Integer[2]);
					indexesE.get(counterE)[0]=i;
					indexesE.get(counterE)[1]=j;
					counterE++;
				}
			}
		}

    }
    public static void initForEachEmptyIndex() {
    	
	    costing = 0;
	    indexesPass.add(new Integer[2]);
	    indexesPass.get(0)[0]=xKitchenIndex;
		indexesPass.get(0)[1]=yKitchenIndex;
		countIndexesPass++;
		for (int i=0;i<height;i++) {
			for(int j=0;j<width;j++) {
				if(floorMap.get(i)[j].type!="W") {
					floorMap.get(i)[j].visited=false;
					floorMap.get(i)[j].calculated=false;
					floorMap.get(i)[j].sumMinPass = Integer.MAX_VALUE;
				}
			}
		}
		//After all indexes initialized for Max Value sum path give ZERO to beginning  
		floorMap.get(xKitchenIndex)[yKitchenIndex].changeSumPass(0);
		
    }
    public static void checkingSumResult() {
    	
		if (checkIfKitchenCanBePlaced()) {
			flagCanKithenPLaced = true;
			if (minimalCosting>costing) {
				minimalCosting = costing;
				indexsBestCosting.clear();
				indexsBestCosting.add(new Integer[2]);
				indexsBestCosting.get(0)[0]=xKitchenIndex;
				indexsBestCosting.get(0)[1]=yKitchenIndex;
			}
			else if(minimalCosting==costing) {
				int size = indexsBestCosting.size();
				indexsBestCosting.add(new Integer[2]);
				indexsBestCosting.get(size)[0]=xKitchenIndex;
				indexsBestCosting.get(size)[1]=yKitchenIndex;
			}
		}
    }
    public static void checkAndPrintResults() {
		if(flagCanKithenPLaced) {
			for(Integer[] bestcosting:indexsBestCosting) {
				floorMap.get(bestcosting[0])[bestcosting[1]].type="K";
			}
			printResults();
		}
		else {
			System.out.println("Kitchen cannot be placed there are not accessible Employes ");
		}
    }
    public static void doForEachEmptyIndex() {
		for(Integer[] elementCheck :indexesCheckFrom) {
			xKitchenIndex = elementCheck[0];
		    yKitchenIndex = elementCheck[1];
			//////////////////////Initialization/////////////////////
			//for each new checking of possible Kitchen position 
			//initialize for all indexes that are Empty area indexes
			//Add index to start find way to Employees from it 
			//the sumMinPass counter for minimal path the visited and calculate flags
			//before calculate again and manage those parameters. 
			initForEachEmptyIndex();
			
			//For this index with empty area check if the kitchen was here,
			//what will be the sum from all employees with best, shortest paths
	        calculateBestPaths();
	        
	        //After each Empty area Index check the sum against the minimum sum and replace accordingly
	        checkingSumResult();
		}	
    }
    public static void main(String[] args) throws FileNotFoundException {
    	String filePath = loadFile(args);
    	initFloorMap(filePath);
    	listsEmployeesEmptiesBuild();
    	doForEachEmptyIndex();
		checkAndPrintResults();
	}
    public static void printResults() {
    	System.out.println("Minimal costing from all employees is : "+ minimalCosting);
    	System.out.println("Indexes for best placing the kitchen\nin this Floor Plan:");
		int counterNextLine=1;
		int countPositions = indexsBestCosting.size();
    	for(Integer[] bestcosting:indexsBestCosting) {
    		countPositions--;
			System.out.print("("+bestcosting[0]+","+ bestcosting[1]+")" );
		    if(counterNextLine%7==0 && countPositions>2) {
		    	System.out.println("");
		    }
		    counterNextLine++;
    	}
    	System.out.println("");
    	
    	for(cellFloor[] row : floorMap ) {
    		
    		for (cellFloor cell :row) {
    			System.out.print(cell.type);
    		}
    		System.out.println("");
    	}
    	System.out.println(   "K       :Kitchen\n"
    			            + "W       :Wall\n"
    			            + "E       :Employee\n[SPACE] :empty space");
    }
	public static  void calculateBestPaths() {
		int sumNow;
		int x;
		int y;
		while(!indexesPass.isEmpty()) {
			
			floorMap.get(xKitchenIndex)[yKitchenIndex].isVisited();
			
			int counter = countIndexesPass;
			for(int index=0;index<counter;index++) {
				Integer[] passIndex = indexesPass.get(0);
				x = passIndex[0];
				y = passIndex[1];
				sumNow = floorMap.get(x)[y].sumMinPass + 1;
				floorMap.get(x)[y].isVisited();
				indexesPass.remove(0);
				countIndexesPass--;
				
				if(x-1>-1) {
					if( floorMap.get(x-1)[y].type != "W"  && 
					    !floorMap.get(x-1)[y].visited     && 
					    !floorMap.get(x-1)[y].calculated ) {
						addInexesToList(x-1,y,sumNow);
					}
				}
				if(y-1>-1) {
					if(floorMap.get(x)[y-1].type != "W" && 
					   !floorMap.get(x)[y-1].visited    &&
					   !floorMap.get(x)[y-1].calculated) {
						addInexesToList(x,y-1,sumNow);
					}				
				}
	            if(x+1<height) {
					if(floorMap.get(x+1)[y].type !="W" && 
					   !floorMap.get(x+1)[y].visited   &&
					   !floorMap.get(x+1)[y].calculated) {
						addInexesToList(x+1,y,sumNow);
					}				
				}
				if(y+1<width) {
					if(floorMap.get(x)[y+1].type !="W" && 
					   !floorMap.get(x)[y+1].visited   &&
					   !floorMap.get(x)[y+1].calculated) {
						addInexesToList(x,y+1,sumNow);
					}				
				}
				
			}
		}
		
	}
	public static void addInexesToList(int indexX,int indexY,int sum) {
		indexesPass.add(new Integer[2]);
		indexesPass.get(countIndexesPass)[0]=indexX;
		indexesPass.get(countIndexesPass)[1]=indexY;
		countIndexesPass++;
		
		floorMap.get(indexX)[indexY].changeSumPass(sum);
		floorMap.get(indexX)[indexY].calculated=true;
		if(floorMap.get(indexX)[indexY].type=="E") {		
			costing = costing + sum;
		}
	}
	public static boolean  checkIfKitchenCanBePlaced() {
	    for (Integer[] index:indexesE) {
	    	if (!floorMap.get(index[0])[index[1]].calculated) {
	    		return false;
	    	}
	    }
		return true;
		
	}
}
