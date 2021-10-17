package rpc;

public class Client {
  public static void main(String [] arg) {
    Matlab m = new Matlab_stub();
    Result res = m.calcul(3);
    System.out.println("->" + res);
  }
}