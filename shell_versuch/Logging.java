public class Logging{
        public final static int DEBUG = 0;
        public final static int INFO = 1;
        public final static int LOGGING = 2;
	
	public static int LVL = LOGGING;
	
	public static void debug(Object str, Object... o){
		if (LVL<=DEBUG)
			System.out.printf(" >>DEBUG>> "+str.toString()+"\n",o);
	}
	public static void info(Object str, Object...o){
		if(LVL<=INFO){
			System.out.printf(" >>INFO >> "+str.toString()+"\n",o);
		}
	}
	public static void log(Object str, Object...o){
                if(LVL<=LOGGING){
                        System.out.printf(" >>LOG  >>"+str.toString()+"\n",o);
                }
        }
}
