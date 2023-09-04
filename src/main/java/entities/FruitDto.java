package entities;

public class FruitDto {

    private String name;
    private String description;

    public FruitDto() {}

    public FruitDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // getters and setters omitted for brevity


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
