package rpc;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;




public class Compilateur {
    
    
    public Method[] getInterface (String Interface_name) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Class<?> clazz = Class.forName("rpc."+Interface_name);
        return clazz.getMethods();
        
    }

    public String[] getParameterNames(Method m) {
        Parameter[] parameters = m.getParameters();
        String[] paramNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            
         paramNames[i] = "param_"+parameters[i].getName();
        }
        return paramNames;
       }
    public void create_boot(String portSocket, String interface_name)throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Method[] mymethod=this.getInterface(interface_name);
        String code="package rpc; \n\t\t"+
        "public class "+interface_name+"_boot {\n\t\t"+
        
        "public static void main(String [] arg) throws Exception { \n\t\t"+
        
        
        "java.net.ServerSocket sos = new java.net.ServerSocket("+portSocket+"); \n\t\t"+
        "java.net.Socket s = sos.accept();\n\t\t"+
        "java.io.DataInputStream dis = new java.io.DataInputStream(s.getInputStream()); \n\t\t"+
        "java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream()); \n\t\t"+
        "String fonction = dis.readUTF(); \n\t\t"+
        interface_name+"_impl maclasse = new "+interface_name+"_impl(); \n\t\t";
        for(Method method : mymethod){
            code += "if (fonction.equals(\""+method.getName()+"\")) { \n\t\t"+
                "oos.writeObject(maclasse."+method.getName()+"(dis.readInt())); \n\t"+
              "}";
        }
        code +="} \n\t }";
        try{
        File monboot= new File(interface_name+"_boot.java");
        if(monboot.createNewFile()){
            System.out.println("File created ");

        }else{
            System.out.println("File already exist");
        }
        FileWriter monwriter= new FileWriter(monboot);
        monwriter.write(code);
        monwriter.close();
        }catch(IOException e){
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
    }
    

    public void create_stub(String portSocket,String  interface_name, String IP) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        
        Method[] mymethod=this.getInterface(interface_name);
        
        String code="package rpc; \n "+
        "import java.io.*;\n"+
        "import java.net.*;\n"+
        "public class "+interface_name+"_stub implements "+interface_name+" { \n\t\t";
        
        for(Method m : mymethod){
            code += "public "+m.getReturnType().getSimpleName()+" "+m.getName()+"(";
            int i=0;
            for(Parameter p:m.getParameters()){
                code += p.getType().getName()+" "+p.getName();
                i++;
                if(i<m.getParameterCount()){
                    code += ",";
                }
            }
            code += " ) throws Exception {\n\t\t"+
        
        
            "java.net.Socket s = new java.net.Socket(\""+IP+"\","+portSocket+"); \n\t\t"+
            "java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream()); \n\t\t"+
            "java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream()); \n\t\t"+
            "dos.writeUTF(\""+m.getName()+"\"); \n\t\t";
            
            
            
            for(Parameter p:m.getParameters()){
                if(p.getType().getName().equals("int")){
                code += "dos.writeInt("+p.getName()+"); \n\t\t";
                }else if(p.getType().getName().equals("String")){
                code += "dos.writeUTF("+p.getName()+"); \n\t\t";
                }
            }
            code += "return(("+m.getReturnType().getSimpleName()+")ois.readObject());";
            
            code +="} \n\t }";
        }
        try{
        File monstub= new File(interface_name+"_stub.java");
        if(monstub.createNewFile()){
            System.out.println("File created ");

        }else{
            System.out.println("File already exist");
        }
        FileWriter monwriter= new FileWriter(monstub);
        monwriter.write(code);
        monwriter.close();
        }catch(IOException e){
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
    

    } 

    public static void main(String [] args) throws Exception {
        Compilateur c = new Compilateur();
        if (args.length<3){
            System.out.println(" Nombre d'arguments insuffisants, on doit avoir : socket interfce_name et ip");
        }
        c.create_stub(args[0], args[1], args[2]);
        c.create_boot(args[0], args[1]);
    }
}
