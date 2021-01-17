public class GrammerScanner {
    protected String checkString = "";
    protected int[] aim;
    private int i = 0;
    private int row = 1;
    private boolean kk = false;//判断是否有出错
    private boolean whetherCorrect = true;
    private boolean ifEnter = true;
    private boolean check = false;


    public GrammerScanner(int[] aim) {//构造方法
        this.aim = aim;
    }

    public void lrparser() {
        if (aim[i] != 1) {//如果第一行少了begin
            System.out.println("error:第" + row + "行：缺少begin");
            whetherCorrect = false;
        }
        i++;
        yucu();
        if (aim[i] == 6) {
            i++;
            if (aim[i] == 0 && whetherCorrect) {
                System.out.println("success!");
            } else if (aim[i] != 0) {
                System.out.println("error:第" + row + "行：缺少#");
                whetherCorrect = false;
            }
        } else {
            System.out.println("error:第" + row + "行：缺少end");
            whetherCorrect = false;
        }
    }

    public void yucu() {
        statement();
        while (aim[i] == 30 || aim[i] == 31 || aim[i] == 26) {
            if ((aim[i] == 30 || aim[i] == 31) && (ifEnter)) {//如果没有分号直接回车了
                System.out.println("error:第" + row + "行：缺少;");
                i++;
                row++;
                whetherCorrect = false;
                ifEnter = false;
            } else if ((aim[i] == 30 || aim[i] == 31)) {
                i++;//如果是注释类的换行，允许
                row++;
            } else if (aim[i] == 26) {
                i++;//如果这是一个分号
                while (aim[i] == 30 || aim[i] == 31) {//如果分号后有一个换行或多行换行，也ok
                    row++;
                    i++;
                }
            }
            statement();
        }
        return;
    }

    public void statement() {
        if (aim[i] == 10) {
            i++;
            ifEnter = true;
            if (aim[i] == 18) {
                i++;
                expression();
            } else {
                System.out.println("error:第" + row + "行：赋值号错误");
                i++;
                whetherCorrect = false;
                expression();
            }
        }
        return;
    }

    public void expression() {
        term();
        while (aim[i] == 13 || aim[i] == 14) {
            i++;
            term();
        }
        return;
    }

    public void term() {
        factor();
        outer:
        while (aim[i] == 15 || aim[i] == 16) {
            if (kk) {//如果一直出错，那么直接调转下一行
                i++;
            }
            i++;
            factor();
        }
        return;
    }

    public void factor() {
        if (aim[i] != 10 && aim[i] != 11) {
            if (aim[i] == 27) {
                i++;
                expression();
                if (aim[i] == 28) {
                    i++;
                } else {
                    System.out.println("error:第" + row + "行：“）”错误");
                    whetherCorrect = false;
                }
            } else {
                System.out.println("error:第" + row + "行：表达式错误,缺少参数");
                kk = true;
                whetherCorrect = false;
                i++;factor();
            }
        } else {
            i++;
        }
        return;
    }
}