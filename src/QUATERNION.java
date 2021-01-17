public class QUATERNION {
    //四元组的结构
    private String op;//操作符
    private String argv1;//第一个操作数
    private String argv2;//第二个操作数
    private String result;//运算结果
    private int nLoop;//获取行号

    //封装
    public int getnLoop() {
        return nLoop;
    }

    public void setnLoop(int nLoop) {
        this.nLoop = nLoop;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getArgv1() {
        return argv1;
    }

    public void setArgv1(String argv1) {
        this.argv1 = argv1;
    }

    public String getArgv2() {
        return argv2;
    }

    public void setArgv2(String argv2) {
        this.argv2 = argv2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
