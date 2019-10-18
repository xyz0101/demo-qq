package Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTool {
    public static String f(Date d){
    	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	 
    	 return df.format(d);
     }
}
