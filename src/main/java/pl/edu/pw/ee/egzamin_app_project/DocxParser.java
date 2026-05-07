package pl.edu.pw.ee.egzamin_app_project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocxParser
{
    Test test;
    File file;

    public void setTest(Test newTest)
    {
        test = newTest;
    }

    public void toDocx()
    {
        List<Question> questions = new ArrayList<>(test.getQuestions());
        String letters = "ABCD";

        StringBuilder testText = new StringBuilder();
        StringBuilder answerSheet = new StringBuilder();

        Collections.shuffle(questions);

        int i = 1;
        for(Question q : questions)
        {
            q.randomizeAnswerOrder();
            testText.append(q.docxString());

            if(!q.isOpen())
            {
                answerSheet.append(i);
                answerSheet.append(letters.charAt(q.getIndexOfCorrectAnswer()));
                answerSheet.append("\n");
            }
            i++;
        }

        System.out.println(testText.toString());
        System.out.println(answerSheet.toString());
    }
}
