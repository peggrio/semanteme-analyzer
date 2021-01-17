public class Word {//封装
    private int typenum;//种别码
    private String word;//扫描得到的词
    public int getTypenum(){
        return typenum;
    }
    public void setTypenum(int typenum){
        this.typenum=typenum;
    }
    public String getWord(){
        return word;
    }
    public void setWord(String word){
        this.word=word;
    }
}
