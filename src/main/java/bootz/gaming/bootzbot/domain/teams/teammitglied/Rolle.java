package bootz.gaming.bootzbot.domain.teams.teammitglied;

public enum Rolle {
    CAPTAIN("Captain"),
    MITGLIED("Mitglied"),
    SUBSTITUTE("Substitute"),
    COACH("Coach");

    private final String rolename;
    Rolle(String rolename) {
        this.rolename=rolename;
    }

    @Override
    public String toString() {
        return this.rolename;
    }
}
