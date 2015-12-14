

public class Validator {

    public Validator() {}

    public String alphabet(String data) {
        if(data.matches("^[a-zA-Z]+$")) {
            return data;
        } else {
            return null;
        }
    }

    public String alphanumeric(String data) {
        if(data.matches("^[a-zA-Z0-9]+$")) {
            return data;
        } else {
            return null;
        }
    }

    public String numeric(String data) {
        if(data.matches("^[0-9]+$")) {
            return data;
        } else {
            return null;
        }
    }

    public String ipv4Address(String data) {
        if(data.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$")) {
            String[] bytes = data.split("\\."); 
            try {
                if(bytes.length == 4) {
                    if(256 > Integer.parseInt(bytes[0]) 
                        && -1  < Integer.parseInt(bytes[0]) 
                        && 256 > Integer.parseInt(bytes[1])
                        && -1  < Integer.parseInt(bytes[1])
                        && 256 > Integer.parseInt(bytes[2])
                        && -1  < Integer.parseInt(bytes[2])
                        && 256 > Integer.parseInt(bytes[3])
                        && -1  < Integer.parseInt(bytes[3])
                    ) {
                        return data;
                    } 
                    return null;
                } 
                return null;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public Integer getInt(String data) {
        if(data.matches("^[0-9]+$")) {
            Integer val = null;
            try {
                val = Integer.parseInt(data);
            } catch(NumberFormatException nfe) {
                return null;
            }
            if(val != null) {
                return val;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
