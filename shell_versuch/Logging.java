public class Logging{
	public final static int LVL = 0;
        public final static int DEBUG = 0;
        public final static int INFO = 1;
        public final static int NORMAL = 2;
	public static void debug(String str, Object... o){
		if (LVL<=DEBUG)
			System.out.printf(">>DEBUG>> "+str+"\n",o);
	}
}
