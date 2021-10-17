package rpc; 
		public class Matlab_boot {
		public static void main(String [] arg) throws Exception { 
		java.net.ServerSocket sos = new java.net.ServerSocket(1334); 
		java.net.Socket s = sos.accept();
		java.io.DataInputStream dis = new java.io.DataInputStream(s.getInputStream()); 
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream()); 
		String fonction = dis.readUTF(); 
		Matlab_impl maclasse = new Matlab_impl(); 
		if (fonction.equals("calcul")) { 
		oos.writeObject(maclasse.calcul(dis.readInt())); 
	}} 
	 }