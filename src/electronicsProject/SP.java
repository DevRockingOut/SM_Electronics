package electronicsProject;

// [WTF_QUESTION]  Where are original standard procedures defined???
public class SP {
	
	// add a part to input conveyor
	static void InsertQue(InputConveyor conveyor, Part part) {
		conveyor.list.add(part);
		conveyor.n += 1;
	}
	
	// add a part to buffer conveyor
	static void InsertQue(BuffConveyor conveyor, Part part) {
		conveyor.list.add(part);
		conveyor.n += 1;
	}
	
	// remove the part at the head from the buffer conveyor
	static Part RemoveQue(BuffConveyor conveyor) {
		if(conveyor.n == 0) {
			return Part.NO_PART;
		}
		
		Part part = conveyor.list.get(0);
		conveyor.list.remove(0);
		conveyor.n -= 1;
	
		return part;
	}
	
	// remove the part at the head from the input conveyor
	static Part RemoveQue(InputConveyor conveyor) {
		if(conveyor.n == 0) {
			return Part.NO_PART;
		}
		
		Part part = conveyor.list.get(0);
		conveyor.list.remove(0);
		conveyor.n -= 1;
		
		return part;
	}
	

}
