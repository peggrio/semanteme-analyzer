import java.util.Scanner;

public class WordsScanner {
    protected static String _KEY_WORD_END="end string of string";
    protected int charNum=0;
    protected Word word;

    protected char[] input=new char[255];
    protected char[] token=new char[255];
    protected int p_input=0;
    protected int p_token=0;

    protected char ch;

    protected String[] keyWord={"begin","if","then","while","do","end","",_KEY_WORD_END};

    public WordsScanner(char[] input){
        this.input=input;
    }
    //取下一个字符

    public char m_getch(){
        if(p_input<input.length){
            ch=input[p_input];
            p_input++;
        }
        return ch;
    }
    //如果是标识符或者空白符就取下一个字符

    public void getbc(){
        while((ch==' '||ch=='\t')&&p_input<input.length){
            ch=input[p_input];
            p_input++;
        }
    }

    //把当前字符和原有字符串连接

    public void concat(){
        token[p_token]=ch;
        p_token++;
        token[p_token]='\0';
    }
    //判断是否是字母
    public boolean letter(){
        if(ch>='a'&&ch<='z'||ch>='A'&&ch<='Z')
            return true;
        else
            return false;
    }
    //判断是否是数字
    public boolean digit(){
        if(ch>='0'&&ch<='9') {
                return true;
        }
            return false;
    }
    //判断是否是小数点
    public boolean dot(){
        if(ch=='.'){
            return true;
        }
        return false;
    }

    //回退一个字符

    public void retract(){
        p_input--;
    }

    //查看token中的字符串是否是关键字，是的话返回关键字种别编码，否则返回10

    public int reserve() {
        int  i=0;
        while(keyWord[i].compareTo(_KEY_WORD_END)!=0) {
            if(keyWord[i].compareTo(new String(token).trim()) == 0) {
                return i+1;
            }
            i++;
        }
        return 10;
    }

    //识别换行，单行注释，多行注释
    //换行的种别码：30
    //多行的种别码：31

    public Word scan() {
        token = new char[255];
        Word myWord = new Word();
        myWord.setTypenum(10);
        myWord.setWord("");

        p_token = 0;
        m_getch();
        getbc();
        if (letter()) {
            while (letter() || digit()) {//如果字符的后面是数字或字符
                concat();
                m_getch();
            }
            retract();
            myWord.setTypenum(reserve());
            myWord.setWord(new String(token).trim());//输出token中的数字串字符形式
            return myWord;
        }else if(digit()) {
            boolean dotOnlyOnce=true;
            while(digit()||(dot()&&dotOnlyOnce)) {//如果数字的后面还是数字,或小数点
                concat();
                if (dot()) {//如果数字的后面是小数点,那么只允许出现一次
                    dotOnlyOnce = false;
                }
                m_getch();
            }
            retract();
            myWord.setTypenum(11);
            myWord.setWord(new String(token).trim());	//输出token中的数字串字符形式
            return myWord;
        } else
            switch (ch) {
                case '=':
                    myWord.setTypenum(25);
                    myWord.setWord("=");
                    return myWord;
                case '+':
                    myWord.setTypenum(13);
                    myWord.setWord("+");
                    return myWord;
                case '-':
                    myWord.setTypenum(14);
                    myWord.setWord("-");
                    return myWord;
                case '*':
                    myWord.setTypenum(15);
                    myWord.setWord("*");
                    return myWord;
                case '/':    //识别换行，单行注释，多行注释
                    //换行的种别码：30
                    //多行的种别码：31
                    m_getch();
                    //识别单行注释
                    if (ch == '/') {
                        String string = "";
                        while (m_getch() != '\n') {
                            retract();
                            string += ch;
                            m_getch();
                        }
                            myWord.setTypenum(30);
                            myWord.setWord(string);
                            return myWord;

                    }
                    //识别多行注释
                    if (ch == '*') {
                        String string = "";
                        int hang=0;
                        while (true) {
                            m_getch();
                            if (ch == '*') {//注释的结尾
                                if (m_getch() == '/') {
                                    myWord.setTypenum(31);
                                    myWord.setWord(string);
                                    return myWord;
                                }
                                retract();
                            }else {
                                if(ch=='\n'){continue;}else{string += ch;}
                            }
                        }
                    }
                    retract();
                    myWord.setTypenum(16);
                    myWord.setWord("/");
                    return myWord;
                case ':':
                    m_getch();
                    if (ch == '=') {
                        myWord.setTypenum(18);
                        myWord.setWord(":=");
                        return myWord;
                    }
                    retract();
                    myWord.setTypenum(17);
                    myWord.setWord(":");
                    return myWord;
                case '<':
                    m_getch();
                    if (ch == '=') {
                        myWord.setTypenum(22);
                        myWord.setWord("<=");
                        return myWord;
                    } else if (ch == '>') {
                        myWord.setTypenum(21);
                        myWord.setWord("<>");
                        return myWord;
                    }
                    retract();
                    myWord.setTypenum(20);
                    myWord.setWord("<");
                    return myWord;
                case '>':
                    m_getch();
                    if (ch == '=') {
                        myWord.setTypenum(24);
                        myWord.setWord(">=");
                        return myWord;
                    }
                    retract();
                    myWord.setTypenum(23);
                    myWord.setWord(">");
                    return myWord;
                case ';':
                    myWord.setTypenum(26);
                    myWord.setWord(";");
                    return myWord;
                case '(':
                    myWord.setTypenum(27);
                    myWord.setWord("(");
                    return myWord;
                case ')':
                    myWord.setTypenum(28);
                    myWord.setWord(")");
                    return myWord;
                case '\n':
                    myWord.setTypenum(30);
                    myWord.setWord("\\n");
                    return myWord;
                case '#':
                    myWord.setTypenum(0);
                    myWord.setWord("#");
                    return myWord;
                //报错
                default:
                    concat();
                    myWord.setTypenum(-1);
                    myWord.setWord("ERROR INFO: WORD = \"" + new String(token).trim() + "\"");
                    return myWord;
            }
    }
    }
