package data_handling;

import java.io.*;

final public class datasets {


    private String datasetname;
    private String data;
    private File fl;
    private String[] document_names;
    private boolean is_dir;
    private boolean file_existence;
    private int n_csv_files;
    private int n_txt_files;
    private String[] labels;



    public datasets(String str){
        this.datasetname = str;
        n_csv_files = 0;
        n_txt_files = 0;
        labels = null;
    }
    public datasets(String str,String path){
        this.datasetname = str;
        n_csv_files = 0;
        n_txt_files = 0;
        labels = null;
        createFile(path);
        filedescr();
        count_files();
    }
    public datasets(){
        datasetname = null;
        fl = null;
        n_txt_files = 0;
        n_csv_files = 0;
        labels = null;
    }

    private void createFile(String path){
        if(path != null) {
            fl = new File(path);
        }
    }

    public void set_path(String path){
        createFile(path);
        filedescr();
        count_files();
    }
    public String get_path(){return fl.getPath();}

    public void set_datasetname(String str){ this.datasetname = str;}
    public String get_datasetname(){return datasetname;}


    private boolean[] file_verification( File f,boolean set_f) {
        boolean read = false,write = false;
        if(!f.exists()) {//System.out.println("invalid path given");
                         if(set_f) file_existence = false;
                         return new boolean[]{false,false};}
        if(set_f)  file_existence = true;
        if(f.canRead()){ read = true;}
        if(f.canWrite()){ write= true;}

        return new boolean[]{read,write};
    }
    private void filedescr(){
        boolean[] temp = file_verification(fl,true);
        if(file_existence) {
            if (fl.isDirectory()) {
                is_dir = true;
                //System.out.println("Given path points to a directory");
                if (!temp[0]) {
                    //System.out.println("directory unaccessible");
                } else {
                    if (fl.isDirectory()) {
                        document_names = fl.list((fl, str) -> {
                            return str.endsWith("csv") || str.endsWith("txt");
                        });
                    }
                }
            } else {//System.out.println("Given path points to a file. Directory required");
                 is_dir = false;}
        }
    }


    private void count_files(){
        if(is_dir && file_existence) {
            for (String s : document_names) {
                if (s.endsWith("csv")) n_csv_files++;
                else n_txt_files++;
            }
        }
    }

    public String[] getDocument_names(){return document_names;}
    public void show_dataset_files(){
        if(file_existence && is_dir) {
            System.out.println("The Number of files are as given under:\ncsv files: " + n_csv_files);
            System.out.println("txt files: " + n_txt_files);

            System.out.println("The files are listed as under:");
            for (String s : document_names) {
                System.out.print(s + ": ");
                boolean[] temp = file_verification(new File(fl + "/" + s),false);
                String str = "";
                str = temp[0] ? str + "readable" : str + "Unreadable";
                str = str + " and ";
                str = temp[1] ? str + "writable" : str + "Unwriteable";
                System.out.println(str);
            }
        }
        else{
            System.out.println("File either dont exist or the given path isnt a valid directory");
        }
    }

    public void load_dataset() throws IOException{
        String path;

        for(String s: document_names) {
            path = fl.getAbsolutePath();
            path = path +"/"+ s;
            System.out.println("Current file to be read :"+s+"\nRead the data or not(Y/N)?");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String ch = br.readLine();


            if(ch.equals("Y") || ch.equals("y")) {
                StringBuilder str = new StringBuilder();
                try (BufferedReader fin = new BufferedReader(new FileReader(path))) {
                    while (fin.ready()) {
                        if (str.length() == 0) str.append(fin.readLine());
                        else str.append(fin.readLine());

                        str.append("\n");
                    }
                } catch (Exception e) {
                    System.out.println("Some Error Occured");
                }
                finally{
                    if(data == null) data = str.toString();
                    else data = data+ str.toString();
                }
            }
        }
    }
    public void load_labels(String label_path){
        boolean temp[] = file_verification(new File(label_path),false);
        if(temp[0]){
            StringBuilder str = new StringBuilder();
            try (BufferedReader fin = new BufferedReader(new FileReader(label_path))) {
                while (fin.ready()) {
                    if (str.length() == 0) str.append(fin.readLine());
                    else str.append(fin.readLine());

                    str.append("\n");
                }
            }
            catch(Exception e){System.out.println("Some error occured");}
            finally{
                labels = str.toString().split(",|' '|");
            }
        }
    }

    public Object[][] convertdata(){
        return data_converter.convert(this.get_data());
    }
    public <T extends Number>T[][] convertdata(data_converter c_algo){
        return (T[][])c_algo.Convert(this.get_data());
    }

    public String[] get_labels(){return labels;}
    public String get_data(){return data;}
}
