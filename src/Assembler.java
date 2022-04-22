import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Assembler{
    private final static File machineLanguageFile = new File("machineLanguageTest.txt");
    private final static File assemblyLanguageFile = new File("assemblyFile.txt");
    private static int address = 0;
    private static StringBuilder binaryCode;
    public static void main(String[] args) {

        List<String []> allOrder=read();
        write("v2.0 raw\n");
        for (int i = 0; i < allOrder.size(); i++) {
            String [] order = allOrder.get(i);
            switch (order[0]){
                case "lw":
                    lw(order[1],order[2]);
                    break;
                case "sw":
                    sw(order[1],order[2]);
                    break;
                case "add":
                    rType("010",order[1],order[2],order[3]);
                    break;
                case "sub":
                    rType("110",order[1],order[2],order[3]);
                    break;
                case "and":
                    rType("000",order[1],order[2],order[3]);
                    break;
                case "or":
                    rType("001",order[1],order[2],order[3]);
                    break;
                case "slt":
                    rType("111",order[1],order[2],order[3]);
                    break;
                case "sne":
                    rType("100",order[1],order[2],order[3]);
                    break;
                case "aad":
                    rType("011",order[1],order[2],order[3]);
                case "asub":
                    rType("101",order[1],order[2],order[3]);
                    break;
                case "addi":
                    iType("00",order[1],order[2],order[3]);
                    break;
                case "aadi":
                    iType("01",order[1],order[2],order[3]);
                    break;
                case "asubi":
                    iType("10",order[1],order[2],order[3]);
                    break;
                case "beq":
                    beq(order[1],order[2],order[3]);
//                        i = Integer.parseInt(order[3]);
                    break;
                case "jal":
                    jType("110",order[1]);
                    break;
                case "jr":
                    jType("111",order[1]);
                    break;
                case "j":
                    jType("101",order[1]);
                    break;

            }
        }
    }
    //read assembly instructions from file
    private static List<String[]> read(){
        Scanner scanner=null;
        List<String []> allOrders = new ArrayList<>();
        String [] order;
        try {
            assemblyLanguageFile.createNewFile();
            scanner =new Scanner(assemblyLanguageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()){
            order=scanner.nextLine().split(" ");
            allOrders.add(order);
        }
        return allOrders;
    }
    //write in file for logisim
    private static void write(String binaryCode ){
        FileWriter fileWriter;
        try {
            machineLanguageFile.createNewFile();
            fileWriter = new FileWriter(machineLanguageFile,true);
            fileWriter.write(binaryCode);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void lw(String rt , String rs){
        binaryCode= new StringBuilder("001");
        int registerName=rs.lastIndexOf('R');
        if(registerName>0)
            binaryCode.append(setRegisterNumber(rs.substring(registerName, registerName + 2))).append(setRegisterNumber(rt));
        else{
            binaryCode.append(setRegisterNumber("zero")).append(setRegisterNumber(rt));
            registerName=rs.indexOf('z');
        }
        int offset = Integer.parseInt(rs.substring(0,registerName-1));
        String offsetString = Integer.toBinaryString(offset);
        for (int i = 0; i < 9 - offsetString.length(); i++) {
            binaryCode.append("0");
        }
        binaryCode.append(offsetString);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);

    }

    private static void sw(String rs , String rt){
        binaryCode= new StringBuilder("010");
        int registerName=rt.lastIndexOf('R');
        if (registerName>0)
            binaryCode.append(setRegisterNumber(rs)).append(setRegisterNumber(rt.substring(registerName, registerName + 2)));
        else {
            binaryCode.append(setRegisterNumber(rs)).append(setRegisterNumber("zero"));
            registerName=rt.indexOf('z');
        }
        int offset = Integer.parseInt(rt.substring(0,registerName-1));
        String offsetString = Integer.toBinaryString(offset);
        for (int i = 0; i < 9 - offsetString.length(); i++) {
            binaryCode.append("0");
        }
        binaryCode.append(offsetString);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);
    }

    private static void rType(String function, String rd , String rs , String rt){
        binaryCode = new StringBuilder("000");
        binaryCode.append(setRegisterNumber(rs));
        binaryCode.append(setRegisterNumber(rt));
        binaryCode.append(setRegisterNumber(rd));
        binaryCode.append("0000");
        binaryCode.append(function);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);

    }

    private static void iType( String function ,String rt , String rs , String immediate){
        binaryCode= new StringBuilder("100");
        binaryCode.append(setRegisterNumber(rs));
        binaryCode.append(setRegisterNumber(rt));
        binaryCode.append(function);
        String binaryImmediate = Integer.toBinaryString(Integer.parseInt(immediate));
        for (int i = 0; i < 7 - binaryImmediate.length(); i++) {
            binaryCode.append("0");
        }
        binaryCode.append(binaryImmediate);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);
    }

    private static void beq(String rs , String rt , String label){
        binaryCode = new StringBuilder("011");
        binaryCode.append(setRegisterNumber(rs));
        binaryCode.append(setRegisterNumber(rt));
        String binaryLabel = Integer.toBinaryString(Integer.parseInt(label));
        for (int i = 0; i < 9 - binaryLabel.length(); i++) {
            binaryCode.append("0");
        }
        binaryCode.append(binaryLabel);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);
    }

    private static void jType(String opcode , String label){
        binaryCode = new StringBuilder(opcode);
        String binaryLabel = Integer.toBinaryString(Integer.parseInt(label));
        for (int i = 0; i < 13 - binaryLabel.length(); i++) {
            binaryCode.append("0");
        }
        binaryCode.append(binaryLabel);
        String hexNum = Integer.toString(Integer.parseInt(binaryCode.toString(),2),16);
        write(hexNum+" ");
        System.out.println(binaryCode);

    }

    private static String setRegisterNumber(String register){
        switch (register){
            case "zero":
                return "00";
            case "R0":
                return "01";
            case "R1":
                return "10";
            default:
                return "11";
        }
    }
}

//    asubi R0 R1 20
//        aadi R0 R1 31
//        aad R1 R1 R0
//        asub R1 R1 R0

//    lw $R1,0($r0)
//        addi $R2,$R0,6
//        slt $R1,$R1,$R2
//        jal myproc
//        add $R1,$R1,$R2
//        beq $R1,$R2,30
//        sw $R2,33($R0)
//        دستور خاص هر گروه (در صورتی که دستور پرش شرطی دارید بعد از رسیدن به مقصد دستور j 50 را اجرا کنید.)
//        j 50
//        myproc: sub $R2,$R1,$R0
//        and $R1,$R2,$R1
//        or $R1,$R1,$R0
//        jr $Ra





/*
    addi R0 zero 0
        j 3
        add R0 R1 zero
        add R1 zero zero
        lw R2 0(R0)
        add R1 R1 R2
        lw R2 1(R0)
        add R1 R1 R2
        lw R2 2(R0)
        add R1 R1 R2
        lw R2 3(R0)
        add R1 R1 R2
        lw R2 4(R0)
        add R1 R1 R2
        lw R2 5(R0)
        add R1 R1 R2
        lw R2 6(R0)
        add R1 R1 R2
        lw R2 7(R0)
        add R1 R1 R2
        lw R2 8(R0)
        add R1 R1 R2
        lw R2 9(R0)
        add R1 R1 R2
        j 2
        */
