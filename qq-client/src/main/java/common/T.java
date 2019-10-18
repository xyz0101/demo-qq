package common;


public class T {
	public void print(String[] str){
		
		for(int i=0;i<str.length;i++){
			System.out.println("String "+str[i]+"=null;");
			System.out.println(str[i]+"=rs.getString("+"\""+str[i]+"\""+");");
		}
		System.out.println("-----------");
		System.out.print( "Book b=new Book(");
		for(int i=0;i<str.length;i++){
			System.out.print(str[i]+",");
			
		}
		System.out.print(");");
	}
	
	public void printbean(String[] str){
		for(int i=0;i<str.length;i++){
			System.out.println("private String "+str[i]+";");
			
		}
		
	}
	
	public static void main(String[] args) {
		// String[] str = new String[]{"user_id","user_name","user_pass","user_sex","user_birth","user_edu","user_blood","user_location","user_date","user_phone","user_email","user_head"};
		//	new T().print(str);
		String[] str = new String[]{"user_id","icon_id","user_state","send_to","chat_model"};
		new T().printbean(str);
	}
}
