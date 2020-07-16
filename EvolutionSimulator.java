import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EvolutionSimulator {// this class only for correctly displaying GUI
    public static int ticks = 0;
    private static final JFrame frame = new JFrame("Evolution simulator");
    private static final JPanel panel_top = new JPanel();
    private static final JPanel panel_east = new JPanel();
    private static final JPanel panel_west = new JPanel();
    private static final JButton button_start = new JButton("Start");
    private static final JButton button_stat = new JButton("Statistic");
    private static final JButton button_spawn = new JButton("Spawn");
    private static final JButton button_stop = new JButton("Stop");
    private static final JButton button_remove = new JButton("Remove all");
    public static JCheckBox checkbox_respawn = new JCheckBox("Respawn");
    public static JLabel label_ticks = new JLabel("");
    public static JTextField textfield_genom = new JTextField("32");
    private static final JLabel label_genom = new JLabel("genome:");
    public static JTextField textfield_allowed_gen = new JTextField("16");
    private static final JLabel label_allowed_gen = new JLabel("allowed mutation:");
    public static JTextField textfield_allowed_gen_start = new JTextField("18");
    private static final JLabel label_allowed_gen_start = new JLabel("allowed mutation on the first gen.:");
    public static JTextField textfield_diff_genom = new JTextField("4");
    private static final JLabel label_diff_genom = new JLabel("difference genome:");
    public static JTextField textfield_size_being = new JTextField("5");
    private static final JLabel label_size_being = new JLabel("size of beings:");
    public static JTextField textfield_energy_start = new JTextField("15");
    public static JLabel label_energy_start = new JLabel("energy on start:");
    public static JTextField textfield_energy_eat= new JTextField("2");
    public static JLabel label_energy_eat = new JLabel("+energy when eating:");
    public static JTextField textfield_energy_minus = new JTextField("-1");
    public static JLabel label_energy_minus = new JLabel("+energy in one tick:");
    public static JTextField textfield_energy_minus_birth = new JTextField("2");
    public static JLabel label_energy_minus_birth = new JLabel("+energy during reproduction:");
    public static JTextField textfield_energy_minus_moving = new JTextField("-1");
    public static JLabel label_energy_minus_moving = new JLabel("+energy when moving:");
    public static JLabel label_number_of_being1 = new JLabel("beings now:");
    public static JLabel label_number_of_being2 = new JLabel("");
    public static JLabel label_number_of_death1 = new JLabel("died this spawn");
    public static JLabel label_number_of_death2 = new JLabel("");
    public static JLabel label_number_of_birth1 = new JLabel("born this spawn");
    public static JLabel label_number_of_birth2 = new JLabel("");
    public static JLabel label_number_of_total1 = new JLabel("total beings this spawn:");
    public static JLabel label_number_of_total2 = new JLabel("");
    private static final JLabel label_status = new JLabel("Information:  ");
    private static final JLabel label_status2 = new JLabel("");
    private static final JLabel label_tickss = new JLabel("tick:");



    public static void main(String[] args) {
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(panel_west,BorderLayout.WEST);
        frame.add(new Evolution_field());

        panel_top.setLayout(new GridLayout(0,2));
        panel_west.setLayout(new GridLayout(0,1));
        panel_east.setLayout(new GridLayout(0,2));
        panel_west.add(panel_top);
        panel_west.add(panel_east);

        panel_top.add(button_start);
        panel_top.add(button_stop);
        panel_top.add(button_spawn);
        panel_top.add(button_remove);
        panel_top.add(button_stat);
        panel_top.add(checkbox_respawn);
        panel_top.add(label_size_being);
        panel_top.add(textfield_size_being);
        panel_top.add(label_energy_start);
        panel_top.add(textfield_energy_start);
        panel_top.add(label_genom);
        panel_top.add(textfield_genom);
        panel_top.add(label_diff_genom);
        panel_top.add(textfield_diff_genom);
        panel_top.add(label_allowed_gen);
        panel_top.add(textfield_allowed_gen);
        panel_top.add(label_allowed_gen_start);
        panel_top.add(textfield_allowed_gen_start);
        panel_top.add(label_energy_minus);
        panel_top.add(textfield_energy_minus);
        panel_top.add(label_energy_minus_birth);
        panel_top.add(textfield_energy_minus_birth);
        panel_top.add(label_energy_minus_moving);
        panel_top.add(textfield_energy_minus_moving);
        panel_top.add(label_energy_eat);
        panel_top.add(textfield_energy_eat);

        panel_east.add(label_status);
        panel_east.add(label_status2);
        panel_east.add(label_number_of_total1);
        panel_east.add(label_number_of_total2);
        panel_east.add(label_number_of_being1);
        panel_east.add(label_number_of_being2);
        panel_east.add(label_number_of_birth1);
        panel_east.add(label_number_of_birth2);
        panel_east.add(label_number_of_death1);
        panel_east.add(label_number_of_death2);
        panel_east.add(label_tickss);
        panel_east.add(label_ticks);

        frame.pack();
        frame.setSize(new Dimension(1300,750));
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button_start.addActionListener(e -> Evolution_field.timer.start());
        button_stat.addActionListener(e -> {
            System.out.println(Evolution_field.Being_Posititon);
            for(Being being: Evolution_field.Being_Posititon.keySet()){
                System.out.println(being+"\npos. :"+Evolution_field.Being_Posititon.get(being)+"\nenergy :"+being.energy+"\ngenome:"+being.genome);
                System.out.println("--");
            }
        });
        button_spawn.addActionListener(e -> Evolution_field.spawn());
        button_stop.addActionListener(e -> Evolution_field.timer.stop());
        button_remove.addActionListener(e -> {
            Evolution_field.Being_Posititon.clear();
        });
        checkbox_respawn.addActionListener(e -> {
            if(checkbox_respawn.isSelected()){
                Evolution_field.respawn();
            }
        });
    }
}
class Evolution_field extends JPanel {
    public static int map_size_zero = 0;
    public static int map_size_length = 700;
    public static int _genome;
    public static int _allowed_mutations;
    public static int _allowed_mutations_first_stg;
    public static int _diff_Genome;
    public static int _size_being;
    public static int _start_energy;
    public static int _energy_eat;
    public static int _energy_minus;
    public static int _energy_minus_birth;
    public static int _energy_minus_moving;
    public static int _total_being;
    public static int _born;
    public static int _died;
    public static int _timescale=1;
    public static Random random = new Random();
    public static HashMap<Being,ArrayList<Integer>> Being_Posititon = new HashMap<>();// positions all of the beings
    public static Timer timer = new Timer(_timescale, e -> {
        if(EvolutionSimulator.ticks< _genome){
            EvolutionSimulator.ticks+=1;
        }else {
            EvolutionSimulator.ticks = 0;
        }
        try {
            Evolution_field.update();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.gc();


    });
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.drawLine(map_size_zero,map_size_zero, map_size_length,map_size_zero);
        g2d.drawLine(map_size_length,map_size_zero, map_size_length, map_size_length);
        g2d.drawLine(map_size_length, map_size_length,map_size_zero, map_size_length);
        g2d.drawLine(map_size_zero, map_size_length,map_size_zero,map_size_zero);
        for(Being be : Being_Posititon.keySet()){
            be.paint(g);
        }
        repaint();
    }
    public Evolution_field(){
        spawn();
    }
    public static void spawn(){
        //Information-
        _genome =Integer.parseInt(EvolutionSimulator.textfield_genom.getText());
        _allowed_mutations = Integer.parseInt(EvolutionSimulator.textfield_allowed_gen.getText());
        _allowed_mutations_first_stg = Integer.parseInt(EvolutionSimulator.textfield_allowed_gen_start.getText());
        _diff_Genome = Integer.parseInt(EvolutionSimulator.textfield_diff_genom.getText());
        _size_being = Integer.parseInt(EvolutionSimulator.textfield_size_being.getText());
        _start_energy =Integer.parseInt(EvolutionSimulator.textfield_energy_start.getText());
        _energy_eat=Integer.parseInt(EvolutionSimulator.textfield_energy_eat.getText());
        _energy_minus=Integer.parseInt(EvolutionSimulator.textfield_energy_minus.getText());
        _energy_minus_birth=Integer.parseInt(EvolutionSimulator.textfield_energy_minus_birth.getText());
        _energy_minus_moving=Integer.parseInt(EvolutionSimulator.textfield_energy_minus_moving.getText());
        _total_being=0;
        _died =0;
        _born =0;
        //Information-
        Being being = new Being(1,350,350,null,_start_energy);
    }
    public static void respawn() {
        if(Evolution_field.Being_Posititon.keySet().size()<=0){
            Evolution_field.spawn();
        }
    }

    public static void update(){
        //Information-
        EvolutionSimulator.label_number_of_death2.setText(String.valueOf(_died));
        EvolutionSimulator.label_number_of_birth2.setText(String.valueOf(_born));
        EvolutionSimulator.label_ticks.setText(String.valueOf(EvolutionSimulator.ticks));
        EvolutionSimulator.label_number_of_being2.setText(String.valueOf(Being_Posititon.size()));
        EvolutionSimulator.label_number_of_total2.setText(String.valueOf(_total_being));
        //Information-
        try{
            if(EvolutionSimulator.checkbox_respawn.isSelected()){
                respawn();
            }
            for(Being be : Being_Posititon.keySet()){
                be.update(EvolutionSimulator.ticks);
            }
            for(Being be : Being_Posititon.keySet()){
                be.energy+=Evolution_field._energy_minus;
            }
        }catch (Exception ignored){}
    }

}

//class beings:
//  {Statuses}
//      1-alive
//      2-died
// {_do_}
//      moving(0-7)
//          0-left
//          1-up
//          2-right
//          3-down
//          4-left_up
//          5-right_up
//          6-right_down
//          7-left_down
//      birth(8-15)
//          8-left
//          9-up
//          10-right
//          11-down
//          12-left_up
//          13-right_up
//          14-right_down
//          15-left_down
//      mutation(16)
//          16-mutation
//      bonuses(17-18)
//          17- energy+1
//          18- energy+2

class Being{
    public static ArrayList<Integer> pos = new ArrayList<>();//auxiliary list for updating position in Being_Position
    public HashMap<Integer,Integer> genome = new HashMap<>();
    public int energy;
    public int status;
    public int position_x;
    public int position_y;
    public int width=Evolution_field._size_being-1;
    public int height=Evolution_field._size_being-1;


    public Being(int status,int poz_x,int poz_y,HashMap<Integer,Integer> genome,int energy1){
        this.status = status;
        this.position_x = poz_x;
        this.position_y = poz_y;
        this.energy=energy1;
        if(genome!=null){// if genome exists, genome will be randomly mutated
            this.genome=genome;
            this.genome.put(Evolution_field.random.nextInt(Evolution_field._genome),Evolution_field.random.nextInt(Evolution_field._allowed_mutations));
        }else{
            for(int i = 0; i<=Evolution_field._genome; i++){
                this.genome.put(i,Evolution_field.random.nextInt(Evolution_field._allowed_mutations_first_stg));
            }
        }
        sub_method1();
        Evolution_field._total_being+=1;
        Evolution_field._born +=1;
    }
    public void paint(Graphics graphics){
        Graphics2D g2d = (Graphics2D) graphics.create();
        if(status==1){
            g2d.setColor(Color.red);
        }else if(status==2) {
            g2d.setColor(Color.gray);
        }
        g2d.fillRect(position_x,position_y,width,height);
    }
    public void _do_(int tick) {
        for(Being be:Evolution_field.Being_Posititon.keySet()){
            if(be.status==2){
                Evolution_field.Being_Posititon.remove(be);
                Evolution_field._died +=1;
            }
        }
        if(status==1){// if alive
            if(energy>=0){
                // It have to be in your code, if your beings should change their positions correctly on edges of the map
                if(position_x<Evolution_field.map_size_zero){
                    position_x= Evolution_field.map_size_length;
                    sub_method1();
                }else if(position_x> Evolution_field.map_size_length){
                    position_x=Evolution_field.map_size_zero;
                    sub_method1();
                }else if(position_y<Evolution_field.map_size_zero){
                    position_y= Evolution_field.map_size_length;
                    sub_method1();
                }else if(position_y> Evolution_field.map_size_length){
                    position_y=Evolution_field.map_size_zero;
                    sub_method1();
                }
                // It have to be in your code, if your beings should change their positions correctly on edges of the map
                //---------------------0---------------------
                if(tick==0){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x-Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }
                        }
                    }
                    position_x+=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------1---------------------
                else if (tick==1){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(1)==(position_y-Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(0)==position_x)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }
                        }
                    }
                    position_y-=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------2---------------------
                else if(tick==2){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x+Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }
                        }
                    }
                    position_x+=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------3---------------------
                else if(tick==3){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y+Evolution_field._size_being)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }
                        }
                    }
                    position_y+=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------4---------------------
                else if(tick==4){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x-Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y-Evolution_field._size_being)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }

                        }
                    }
                    position_y-=Evolution_field._size_being;
                    position_x-=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------5---------------------
                else if(tick==5){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x+Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y-Evolution_field._size_being)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }

                        }
                    }
                    position_y-=Evolution_field._size_being;
                    position_x+=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------6---------------------
                else if(tick==6){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x+Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y+Evolution_field._size_being)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }

                        }
                    }
                    position_y+=Evolution_field._size_being;
                    position_x+=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------7---------------------
                else if(tick==7){
                    for(Being be:Evolution_field.Being_Posititon.keySet()){
                        if((Evolution_field.Being_Posititon.get(be).indexOf(0)==(position_x-Evolution_field._size_being)) && (Evolution_field.Being_Posititon.get(be).indexOf(1)==position_y+Evolution_field._size_being)){
                            if(differents(genome,be.genome)>Evolution_field._diff_Genome){
                                energy+=Evolution_field._energy_eat;
                                be.status=2;
                            }else{
                                genome.put(Evolution_field.random.nextInt(Evolution_field._genome +1),be.genome.get(Evolution_field.random.nextInt(Evolution_field._genome +1)));
                            }

                        }
                    }
                    position_y+=Evolution_field._size_being;
                    position_x-=Evolution_field._size_being;
                    energy+=Evolution_field._energy_minus_moving;
                    sub_method1();
                }
                //---------------------8---------------------
                else if(tick==8){
                    Being being = new Being(status,position_x-Evolution_field._size_being,position_y,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------9---------------------
                else if(tick==9){
                    Being being = new Being(status, position_x, position_y - Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------10---------------------
                else if(tick==10){
                    Being being = new Being(status,position_x+Evolution_field._size_being,position_y,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------11---------------------
                else if(tick==11){
                    Being being = new Being(status,position_x,position_y+Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------12---------------------
                else if(tick==12){
                    Being being = new Being(status,position_x-Evolution_field._size_being,position_y-Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------13---------------------
                else if(tick==13){
                    Being being = new Being(status,position_x+Evolution_field._size_being,position_y-Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------14---------------------
                else if(tick==14){
                    Being being = new Being(status,position_x+Evolution_field._size_being,position_y+Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------15---------------------
                else if(tick==15){
                    Being being = new Being(status,position_x-Evolution_field._size_being,position_y+Evolution_field._size_being,genome,energy);
                    sub_method2(being);
                    energy+=Evolution_field._energy_minus_birth;
                }
                //---------------------16---------------------
                else if(tick==16){
                    genome.put(Evolution_field.random.nextInt(Evolution_field._genome),Evolution_field.random.nextInt(Evolution_field._allowed_mutations));
                }
                //---------------------17---------------------
                else if(tick==17){
                    energy+=1;
                }
                //---------------------18---------------------
                else if(tick==18){
                    energy+=2;
                }
            }else{
                status=2;
            }
        }

    }
    public void sub_method1(){
        pos.add(this.position_x);
        pos.add(this.position_y);
        Evolution_field.Being_Posititon.put(this,pos);
        pos.clear();
    }
    public void sub_method2(Being being){
        pos.add(being.position_x);
        pos.add(being.position_y);
        Evolution_field.Being_Posititon.put(being,pos);
        pos.clear();
    }
    public void update(int tick) {
        _do_(genome.get(tick));
    }
    public int differents(HashMap<Integer,Integer> hashMap1, HashMap<Integer,Integer> hashMap2){// find difference between two hashmaps(genom)
        int score = 0;
        for(int i = 0; i<=Evolution_field._genome; i++){
            if(hashMap1.get(i).equals(hashMap2.get(i))){
                score+=1;
            }
        }
        return score;
    }
}