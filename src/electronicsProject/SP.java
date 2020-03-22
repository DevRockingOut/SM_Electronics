package electronicsProject;

// [WTF_QUESTION]  Where are original standard procedures defined???
public class SP {
	
	// add a part to input conveyor
	public static void spInsertQue(InputConveyor conveyor, Part part) {
		conveyor.list.add(part);
		conveyor.n += 1;
	}
	
	// add a part to buffer conveyor
	public static void spInsertQue(BuffConveyor conveyor, Part part) {
		conveyor.list.add(part);
		conveyor.n += 1;
	}
}
