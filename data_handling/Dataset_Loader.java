package data_handling;

import java.io.IOException;

public class Dataset_Loader {

    private datasets[] dt;


    public Dataset_Loader(int n){
        dt = new datasets[n];
    }
    public Dataset_Loader(int n,String name){
        dt = new datasets[n];
        dt[0] = new datasets(name);
    }
    public Dataset_Loader(int n,String name,String path){
        dt = new datasets[n];
        dt[0] = new datasets(name,path);
    }
    public Dataset_Loader(int n,String ... name){
        dt = new datasets[n];

        for(int i = 0;i<dt.length;i++) {
            dt[i] = new datasets(name[i]);
        }
    }
    public Dataset_Loader(int n,String[] path,String ... name){
        dt = new datasets[n];
        int i = 0;
        for(datasets d:dt) {
            d = new datasets(name[i],path[i]);
            i++;
        }
    }

    public void set_paths(String ... path){
        int i = 0;
        for(datasets d : dt){
            d.set_path(path[i]);
            i++;
        }
    }

    public void show_file_desc(){
        for(datasets d: dt){
            d.show_dataset_files();
        }
    }

    public boolean load_data(String dataset_name) throws IOException {

        for(datasets d:dt){
            if(d.get_datasetname().equalsIgnoreCase(dataset_name)) {
                d.load_dataset();
                return true;
            }
        }
        System.out.println("dataset not found");
        return false;
    }
    public datasets get_dataset(String name){
        for(datasets d: dt){
            if(d.get_datasetname().equals(name)){
                return d;
            }
        }
        System.out.println("Invalid dataset name");
        return null;
    }

    public Object[][] convertdata(datasets d){
        return data_converter.convert(d.get_data());
    }
    public <T extends Number>T[][] convertdata(datasets d,data_converter c_algo){
        return (T[][])c_algo.Convert(d.get_data());
    }
}
