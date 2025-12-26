package ua.notion.components;

public class UserPaths {
    private String pathForGameDownload;

    private String pathForGameInstalls;

    private String pathForProtonInstalls;

    private String pathForPrefixes;

    public String getPathForGameDownload() {
        return pathForGameDownload;
    }

    public String getPathForGameInstalls() {
        return pathForGameInstalls;
    }

    public String getPathForProtonInstalls() {
        return pathForProtonInstalls;
    }

    public String getPathForPrefixes() {
        return pathForPrefixes;
    }

    public void setPathForGameDownload(String pathForGameDownload) {
        this.pathForGameDownload = pathForGameDownload;
    }

    public void setPathForProtonInstalls(String pathForProtonInstalls) {
        this.pathForProtonInstalls = pathForProtonInstalls;
    }

    public void setPathForGameInstalls(String pathForGameInstalls) {
        this.pathForGameInstalls = pathForGameInstalls;
    }

    public void setPathForPrefixes(String pathForPrefixes) {
        this.pathForPrefixes = pathForPrefixes;
    }
}
