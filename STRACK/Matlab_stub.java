package rpc; 
 import java.io.*;
import java.net.*;
public class Matlab_stub implements Matlab { 
		public Result calcul(int arg0 ) throws Exception {
		java.net.Socket s = new java.net.Socket("localhost",1334); 
		java.io.DataOutputStream dos = new java.io.DataOutputStream(s.getOutputStream()); 
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream()); 
		dos.writeUTF("calcul"); 
		dos.writeInt(arg0); 
		return((Result)ois.readObject());} 
	 }