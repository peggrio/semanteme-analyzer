import java.io.*;
import java.util.ArrayList;

public class Semanteme {
    protected File inputFile;
    QUATERNION pQuad[]=new QUATERNION[1000];//存放四元组的数组
    int nSuffix,nNXQ,ntc,nfc;//临时变量的编号
    Word uWord;//扫描得到的单词
    int gnRow=1;//行号
    private ArrayList<Word> list;
    private int i=0;
    FileOutputStream fos;
    PrintStream ps;
    private int nLoop=1;
    private int nChain;
    private boolean success=true;//如果全程无错
    private boolean error=false;//如果之前已报错，后面紧挨的错可忽略不计
    private boolean kk=false;

//构造函数
    public Semanteme(String output,ArrayList<Word> list){
        this.list=list;
        for(int i=0;i<1000;i++){//初始化
            pQuad[i]=new QUATERNION();//数组里的每一个值还需要再声明一次
        }
        try {
            this.fos=new FileOutputStream(output);
            this.ps=new PrintStream(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
//-----------------------------------------------------定位语法错误-----------------------------------------------------------------------------------------------
    public void LocateError(int nRow){//定位语法错误
        String str="Error Location: Row:"+nRow+"   ------>  ";
        ps.print(str);
    }
    public void Qerror(String strError){//输出扫描发现的错误
        if(!error){//之前还没有出过错误
        LocateError(gnRow);
        ps.println("缺"+strError);
        success=false;
        error=true;
        }//如果是换行，不要漏了
        if(!(list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34)){
            i++;
            return;
        }
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
        return;
    }
    public void Derror(String str){//输出扫描发现的错误
        if(!error){//之前还没有出过错误
            LocateError(gnRow);
            ps.println(str);
            success=false;
            error=true;
        }//如果是换行，不要漏了
        if(!(list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34)){
            i++;
            return;
        }
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
        return;
    }
    public void Match(int syn,String strError) {
        //判断当前识别出的单词是否是需要的单词，如果不是则报错，否则扫描下一个单词
        if (syn == list.get(i).getTypenum()) {//当前的字符是合法字符
            i++;
        } else {
            Qerror(strError);
            return;
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------

    public void gen(String op,String argv1,String argv2,String result,int nLoop){//生成一个四元式
        pQuad[nLoop].setOp(op);
        pQuad[nLoop].setArgv1(argv1);
        pQuad[nLoop].setArgv2(argv2);
        pQuad[nLoop].setResult(result);
        error=false;
        return;
    }
    public void PrintQuaternion() {//打印四元式数组,一次性打印
        for (int i = 1; i < nLoop; i++) {
            ps.print("(" + (i) + ") " + pQuad[i].getResult() + " " + pQuad[i].getArgv1() + " " + pQuad[i].getOp() + " " + pQuad[i].getArgv2() + "\t\n");
        }
    }
//----------------------------------------------------------------------------------------------------------------------2
    public String Newtemp(){//产生一个临时变量
    String strTempID;
    strTempID="t"+ ++nSuffix;
    return strTempID;
        }

    public String Expression(){
        String opp,eplace1,eplace2;
        String eplace;
        eplace1=Term();
        eplace=eplace1;
        while(list.get(i).getTypenum()==13||list.get(i).getTypenum()==14){//识别+-
            opp=list.get(i).getWord();
            i++;
            eplace2=Term();
            eplace=Newtemp();
            gen(opp,eplace1,eplace2,eplace+" =",nLoop++);
            eplace1=eplace;
        }
        return eplace;
    }
    public String Term(){
        String opp_2,eplace1,eplace2,eplace;
        eplace=eplace1=Factor();
        while(list.get(i).getTypenum()==15||list.get(i).getTypenum()==16){//识别*/
            opp_2=list.get(i).getWord();
            i++;
            eplace2=Factor();
            eplace=Newtemp();
            gen(opp_2,eplace1,eplace2,eplace+" =",nLoop++);
            eplace1=eplace;
        }
        return eplace;
    }
    public String Factor() {
        String eplace;
        if (list.get(i).getTypenum() == 10 || list.get(i).getTypenum() == 11) {//识别letter/digit
            eplace = list.get(i).getWord();
            i++;
        } else {
            Match(27, "(");//判断当前字符是不是"("
            eplace = Expression();
            if (list.get(i).getTypenum() != 28) {
                Derror("符号连接不正确");
                while (list.get(i).getTypenum() != 28) {
                    i++;
                }
            }
                Match(28, ")");//判断当前字符是不是")'
        }
            return eplace;
    }


    public void Condition( int etc,int efc){
        String opp_3,eplace1,eplace2;
        String strTemp;

        eplace1=Expression();
        if((list.get(i).getTypenum()>=20&&list.get(i).getTypenum()<=24)||list.get(i).getTypenum()==12){//判断当前字符是否为<><=>=!===
            opp_3=list.get(i).getWord();
            i++;
            eplace2=Expression();
            ntc=nNXQ;
            nfc=nNXQ+1;
            strTemp=opp_3;
            gen(strTemp,eplace1,eplace2+"  goto  "+(nLoop+2),"if",nLoop++);
        }else{
            Qerror("缺少关系运算符");
        }
    }

    public void Statement(){//语句分析函数
        String strTemp,eplace;
        int nChainTemp =0;
        switch (list.get(i).getTypenum()){
            case 10://如果是标识符
                strTemp=list.get(i).getWord();
                i++;
                Match(25,"=");
                eplace=Expression();
                if (list.get(i).getTypenum() != 26) {
                    if(list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34){
                        Qerror(";");
                    }else{
                        Derror("符号多余");
                        ps.println("源程序非正常结束");
                        System.exit(0);
                    }
                }
                Match(26,";");
                //如果是回车换行注释的话就跳过
                while(list.get(i).getTypenum()==33||list.get(i).getTypenum()==34){
                    i++;gnRow++;
                }
                    gen("=",strTemp,eplace,"",nLoop++);
                nChain=0;
                break;
            case 4:
                Match(4,"if");
                Match(27,"(");
                Condition(ntc,nfc);
                if (list.get(i).getTypenum() != 28) {
                    Derror("符号连接不正确");
                    while (list.get(i).getTypenum() != 28) {
                        i++;
                    }
                }
                Match(28,")");
                int j=(nLoop++);
                Statement_Block();//这时开始遍历的是满足if内条件内的句子
                //this.nChain=merg(nChainTemp,nfc);
                gen("","","", "else goto  "+nLoop,j);////////////////////////////////////////////
                break;
            case 7:
                Match(7,"while");
                Match(27,"(");
                int s=nLoop;//保留记录while所在行号
                Condition(ntc,nfc);
                //bp(ntc,nNXQ);
                if (list.get(i).getTypenum() != 28) {
                    Derror("符号连接不正确");
                    while (list.get(i).getTypenum() != 28) {
                        i++;
                    }
                }
                Match(28,")");
                int m=(nLoop++);
               // nLoop++;
                Statement_Block();
                gen("","","", "else goto  "+(nLoop+1),m);////////////////////////////////////////////
                //bp(nChainTemp,nWQUAD);
                gen("","","","goto "+s,nLoop++);//重新进入while循环
                this.nChain=nfc;
                break;
        }
        return;
    }

    public void Statement_Sequence(int nChain){
        //语句串分析函数
        this.nChain=nChain;
        Statement();
        while(list.get(i).getTypenum()==10||list.get(i).getTypenum()==4||list.get(i).getTypenum()==7){//id,if,while
            //bp(nChain,nNXQ);
            Statement();
        }
        //bp(this.nChain,nNXQ);
        return;
    }
    public void Statement_Block() {//语句块分析函数
        //如果是回车换行注释的话就跳过
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
        Match(31, "{");
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
        Statement_Sequence(nChain);
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
        if(list.get(i).getTypenum()==1000){
            Qerror("}");
            i--;//已经到达最后一个终结符
            kk=true;
            return;
        }
        Match(32, "}");
        while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {
            i++;
            gnRow++;
        }
    }

    public void Parse(){
        int nChain=0;
        Match(1,"main");
        Match(27,"(");
        Match(28,")");
        Statement_Block();
        if(list.get(i).getTypenum()!=1000||kk){
            ps.println("源程序非正常结束");
        }else {
            PrintQuaternion();
            ps.println("(" + (nLoop) + ")" + "-------finished---------");
            if (success) {
                ps.println("success!");
            }
        }
    }

    public void lrparse(){//语法语义分析主函数
        nSuffix=0;
        nfc=ntc=nNXQ=1;
        Parse();
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
