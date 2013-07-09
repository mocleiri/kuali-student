public class StringBuilderLoopUsage {
    
    private String name;
    
    public ContainsStringBuilder (String ...parts) {
        
        StringBuilder buf = new StringBuilder();
        
        StringBuilder buf2 = new StringBuilder("error");
        
        StringBuilder b2 = null;
        
        for (String p : parts) {
            buf.append(p);
        }
        
        this.name = buf.toString();
        
    }
}