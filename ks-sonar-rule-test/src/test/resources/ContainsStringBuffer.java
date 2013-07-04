public class ContainsStringBuffer {
    
    private String name;
    
    public ContainsStringBuffer (String ...parts) {
        
        StringBuffer buf = new StringBuffer();
        
        new StringBuffer();
        
        StringBuffer b2 = null;
        
        for (String p : parts) {
            buf.append(p);
        }
        
        this.name = buf.toString();
        
    }
}