package data_handling;

public interface data_converter {
    static Object[][] convert(String s){
        String[] con = s.split("\n");
        Object[][] temp = new Object[con.length][];

        int i = 0;
        while(i<con.length){
            if(con[i] == null) continue;
            temp[i] = con[i].split(",|' '");
            i++;
        }

        return temp;
    }

    Object[][] Convert(String s);
}
