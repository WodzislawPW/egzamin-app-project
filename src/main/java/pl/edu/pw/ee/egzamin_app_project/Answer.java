package pl.edu.pw.ee.egzamin_app_project;

public class Answer
{
    private String text;
    private boolean correct;

    public Answer(){}

    public Answer(String text, boolean correct)
    {
        this.text = text;
        this.correct = correct;
    }

    public String toString()
    {
        return text;
    }

    public String getText()
    {
        return text;
    }

    public boolean isCorrect()
    {
        return correct;
    }
}
