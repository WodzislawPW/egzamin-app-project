package pl.edu.pw.ee.egzamin_app_project;

public class Setting
{
    private String name;
    private boolean value;

    public Setting(){}

    public Setting(String name, boolean value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
