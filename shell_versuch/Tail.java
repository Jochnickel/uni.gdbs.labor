import cTools.KernelWrapper;
import java.util.LinkedList;
import java.util.Scanner;

public class Tail{
	
//	private static int amt = -10;
	private final static String STDIN = "-";
	private final static int READ_BUFFER_SIZE = 65536;
	public static void main(final String[] args){
		var amt = -10;
		final var filenames = new LinkedList<String>();
		for(int i = 0; i< args.length; i++){
			switch(args[i]){
				case "-n":
					amt = handleMinusN(args,i);
					break;
				case "--help":
					printHelp();
					KernelWrapper.exit(0);
					break;
				case "":
					break;
				default:
					filenames.add(args[i]);
					break;
			}
		}

		if(filenames.size()<1){
			tailSingle(amt, STDIN);
		} else if (filenames.size()<2){
			tailSingle(amt, filenames.element());
		} else {
			tailAll(amt, filenames);
		}
		KernelWrapper.exit(0);
	}
	
	private static void tailStdIn(int amt){
		final var scanner = new Scanner(System.in);
		if(amt>0){
			// skip first n lines
			try{
				for(;amt>0;amt--)
					scanner.nextLine();
				for(;;System.out.println(scanner.nextLine());
			} catch (Exception e){}
		} else {
		// repeat last n lines
			final var list = new LinkedList<String>();
			try{
				for(;list.size()<-amt;)list.add(scanner.nextLine();
				for(;;list.removeFirst())list.add(scanner.nextLine();
			} catch (Exception e){}
			for(final var str : list) System.out.println(str);
		}
		scanner.close();
	}

	private static int seekNNewLines(int amt, final int fd){
		for(;amt!=0;){
			final var b = new byte[1];

			if(amt<0)
				KernelWrapper.lseek(fd,-1, KernelWrapper.SEEK_CUR);
			final var readBytes = KernelWrapper.read(fd, b, 1);
			if(amt<0)
                                KernelWrapper.lseek(fd,-1, KernelWrapper.SEEK_CUR);

			if (readBytes<0)
				KernelWrapper.exit(1);
			if(readBytes<1)
				return;
			if ('\n'==b[0]){
				if(amt>0){
					amt--;
				} else if (amt<0){
					amt++;
				}
			}
		}
		return KernelWrapper.lseek(8, 0,KernelWrapper.SEEK_CUR);
	}

	private static void tailSingle(final int amt, final String filename){
		if(STDIN.equals(filename)){
			tailStdIn(amt);
		} else {
			final var fd = KernelWrapper.open(filename,KernelWrapper.O_RDONLY);
			if(fd<0){
				System.err.printf("cannot open '%s' for reading: No such file or directory",filename);
				KernelWrapper.exit(1);
			}
			final var fileSize = KernelWrapper.lseek(fd,0,KernelWrapper.SEEK_END);
			if(amt>0){
				KernelWrapper.lseek(fd,0,KernelWrapper.SEEK_SET);
			}
			final var somewhere = seekNNewLines(amt, fd);
			final var strLen = fileSize-somewhere;
			final var buf = new byte[strLen];
			if(KernelWrapper.read(fd, buf, strLen)!=strLen){
				KernelWrapper.exit(1);
			}
			System.out.println(new String(buf));
		}
	}
	private static void tailAll(int amt, final LinkedList<String> filenames){
		for(final var fn : filenames){
			System.out.printf("==> %s <==", STDIN.equals(fn) ? "standard input" : fn);
			tailSingle(fn);
		}
	}

	private static int handleMinusN(final String[] args, final int index){
		try{
			final var value = args[index+1].matches("\\d+") ? "-" + args[index+1] : args[index+1] ;
			args[index] = "";
			args[index+1] = "";
			return Integer.valueOf(value);
		} catch(NumberFormatException e){
			System.err.printf("invalid number of lines: '%s'\n", args[index+1]);
			KernelWrapper.exit(1);
		} catch(ArrayIndexOutOfBoundsException e){
			System.err.println("option requires an argument -- 'n'\nTry 'tail --help' for more information.");
			KernelWrapper.exit(1);
		}
	}
	public static void printHelp(){
		System.out.println(
			"Tail help!\n"+
			" tail asd.txt : print last 10 lines of asd.txt\n"+
			" tail asd.txt -n 3 : print last 3 lines\n"+
			" tail asd.txt -n +3 : print everything from line 3\n"+
			" tail "+STDIN+" : waiting for filenames on StdIn \n"+
			" tail --help : print this help\n");
	}
}
