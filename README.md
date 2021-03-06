# Evoluton Simulator
Здесь будет описанна логика программы Evoluton Simulator. На самом деле я даже не знаю является ли это название правильным, но оно мне уже понятно и менять его впадлу.

## Cтруктура
Сама программа стоит из трех классов:
- Evolution Simulator
- Evolution field
- Panel gGenome
- Being

`EvolutionSimulator` - в нем находится все, что связанно с GUI(JPanel,JTextfield,JLabel...)

`Evolution_field` - это по сути само поле, где происходит действие.Также в этом классе будут находиться "настройки", но об этом далее.

`Panel Genom` - класс являющийся новым окном, в котором можно вручную задать "геном"(список действий) первой генерации существ.

`Being` - самый интересный класс программы, где прописанна логика существа.

### Evolution simulator
По моему мнению, человек знакомый с awt и swing, вполне может понять этот класс и его работу без дополнительных обьяснений.

### Evolution field
В самом начале данного класса существуют переменные настроек симуляции.
```
    public static int map_size_zero = 0;
    public static int map_size_length = 700;
```
Отвечают за размер поля.
```
   public static int _genome;
```
Отвечает за количество "генов" в "геноме" существа.(или же по приземленному, количеству действий в списке действий)
```
   public static int _allowed_mutations;
   public static int _allowed_mutations_first_stg;
```
Разрешенная мутацая и разрешенная мутация на первой стадии.
```
   public static int _diff_Genome;
```
Количественная разница генома при сравнении(насколько один список действий отличается от другого)
```
  public static int _size_being;
```
Размер существ. Рекомендую ставить четный. 
```
    public static int _start_energy;
    public static int _energy_eat;
    public static int _energy_minus;
    public static int _energy_minus_birth;
    public static int _energy_minus_moving;
```
Начальная энергия первой генерации. 

Изменение энергии при пожирании других существ. 

Изменение энергии в тик. 

Изменение энергии про рождении другого существа.

Измение энергии при движении.
```
    public static int _total_being;
    public static int _born;
    public static int _died;
```
Эти перменные нужны, чтобы контролировать сколько суцществ было, сколько родилось и сколько умерло.

Также в этом классе есть таймер, считающий тики(ходы) и вызывающий метод `update`:
```
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
```
Словарь в котором находятся все существа и их позиции (x,y), где обьект класса `Being` - зачение, список из его позиции - ключ:
```
    public static HashMap<Being,ArrayList<Integer>> Being_Posititon = new HashMap<>();// positions all of the beings
```

Самый главный метод данного класса - метод `update`.
В нем происходит :

  1 - респавн существ, если все существа предыдущего спавна мертвы.(если в настройках стоит галочка)
  
  2 - вызывания метода `update` у каждого существа из словаря `Being_Position`
  
  3 - изменение энергии у каждого существа
  
### Panel Genom
Этот класс довольно прост, и при взгляде на него не должно быть трудностей.

Метод одноименный с названием класса (`Panel_Genom`)  настраивает и вызывает два дополнительных метода для создания GUI.

Метод `updating_and_sending_genome` считывает то, что настроил пользватель и применяет это.
```
    public void updating_and_sending_genome(){
        for(int i = 0;i<=(Genome_number);i++){
            int textfield_int;
            String textfield_string = textFields.get(i).getText();
            if(textfield_string.equals("")){
                textfield_int = 0;
            }else{
                textfield_int = Integer.parseInt(textfield_string);
            }
            _new_genome.put(i,textfield_int);
        }
        System.out.println(_new_genome);
        Evolution_field.set_new_genome(_new_genome);

    }
```

### Being 
В этом классе существует вспомогательный список `pos`:
```
 public static ArrayList<Integer> pos = new ArrayList<>();//auxiliary list for updating position in Being_Position
```

Геном существа (список действий) в виде словаря, где номер тика - значение, номер действия - ключ:
```
    public HashMap<Integer,Integer> genome = new HashMap<>();
```

Одним из важных методов класса `Being` с одноименным названием: 
```
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
```
Он используется в конструкторе класса, чтобы существо, рожающее данное могла передать ему свой геном, энергию и позицию.
Также здесь исполняется мутация одного гена в геноме, или создание генома заново при отстутвии его при передаче.

Здесь можно заметить вызывание метода `sub_method1`. Помимо него есть еще похожий метод, но используемый при рождении существа - `sub_method2`.
Данные два метода используются лишь для обновления позиции существа с помощью списка `pos` в словаре `Being_Position`.

#### Далее разибраться в технической стороне моей программы по моему мнению не имеет смысла, так как дальнейшая речь будет касаться в большинстве своем логике программы, так что проще рассказать сразу о ней.

## Логика.
Первоначально стоит поговорить о статусах существ: 
1 - существо живое,
2 - мертвое

Если у существа энергия равна 0 или ниже, то оно принимает статус 2 и уничтожается.

Далее, действия который могут производить существа:
* Движение
  * 0 - влево
  * 1 - вверх
  * 2 - вправо
  * 3 - вниз
  * 4 - влево-вверх
  * 5 - вправо-вверх
  * 6 - вправо-вниз
  * 7 - влево-вниз
* Рождение
  * 8 - влево
  * 9 - вверх
  * 10 - вправо
  * 11 - вниз
  * 12 - влево-вверх
  * 13 - вправо-вверх
  * 14 - вправо-вниз
  * 15 - влево-вниз
* Мутация
  * мутация одного гена в геноме
* Бонусы
  * +1 к энергии
  * +2 к энергии
  
  
  При движении: 
  * Если существо достигает края карты, то при движении дальше на край, она выходит с обратной стороны карты на той же высоте или долготе.
  * Если при движении существо наталкивается на другое существо: при разности их геномов выше указанового в настройках, существо-нападающий поедает существо-жертву, забирая ее энергию и меняя ее статуст на "2"(мертвая).Однако если разность меньше или равна указанной, то обе клетки меняются случайными генами из геномов.
  
  
  Рождение происходит передачей энергии и генома с мутацией одного гена от существа-матери к существау-дочери.
  
  Таймер отсчитывает один тик в тысячную секунды. Количество тиков равно колчеству генов в геноме каждого существа, чтобы исключить ошибки.
  
  Когда тики додхят до своего конца, то отсчет начинается заново.
  
  При обновлении (метод `update`) каждому существу посылается информация с текущим тиком, чтобы существо посмотрев в своем геноме, какое действие оно должно произвести на данный тик, произвело его.
  
###### !!!Некоторые методы могут быть слегка изменены или часть их функционала перенесено в новый созданный метод, но это не критично и не должно влиять на логику!!!
