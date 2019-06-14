package com.landsky.camera.entity;


public class DistinguishResult {
    BaseLibrary baseLibrary;
    Person person;

    public BaseLibrary getBaseLibrary() {
        return baseLibrary;
    }

    public void setBaseLibrary(BaseLibrary baseLibrary) {
        this.baseLibrary = baseLibrary;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "{" +
                "baseLibrary=" + baseLibrary +
                ", person=" + person +
                '}';
    }
}
