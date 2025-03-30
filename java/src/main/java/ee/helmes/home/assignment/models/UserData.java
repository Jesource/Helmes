package ee.helmes.home.assignment.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ElementCollection
    private List<Integer> idsOfInvolvedSectors = new ArrayList<>();
    private boolean agreeToTerms;

    public UserData() {
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UserData userData)) return false;

        return agreeToTerms == userData.agreeToTerms
               && Objects.equals(id, userData.id)
               && Objects.equals(name, userData.name)
               && Objects.equals(idsOfInvolvedSectors, userData.idsOfInvolvedSectors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);

        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(idsOfInvolvedSectors);
        result = 31 * result + Boolean.hashCode(agreeToTerms);

        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", idsOfInvolvedSectors=" + Collections.singletonList(idsOfInvolvedSectors) +
               ", agreeToTerms=" + agreeToTerms +
               '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIdsOfInvolvedSectors() {
        return idsOfInvolvedSectors;
    }

    public void setIdsOfInvolvedSectors(List<Integer> idsOfInvolvedSectors) {
        this.idsOfInvolvedSectors = idsOfInvolvedSectors;
    }

    public boolean isAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(boolean agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }
}
