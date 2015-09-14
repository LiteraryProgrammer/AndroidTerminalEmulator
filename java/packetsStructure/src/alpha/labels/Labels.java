package alpha.labels;

/**
 * Created by gpietrus on 23.08.15.
 */
public class Labels {

    //only tempoerary to analyse
    //many formats of labels

    //todo: kiedy sie powinno skonczyc?
    //todo: co powininen zwracac, jaka nowa klase?
    //todo: czy jezeli konczy sie labelem to czy to musi byc koniec calosci?
    //todo: MOZE BYC TYLKO JEDEN POINTER? wtedy sie nie zakoncza \0
    //todo: zakonczenie zerem tylko gdy jest sam label
    //todo: a gdy label + pointer to bez \0


    public static void main(String[] args) {

//        byte bytes[] = {4,100,110,115,49,-64,12,3,100,110,115,-64,12,120,27,-75,-82};
//        byte bytes[] = {4,100,110,115,49,0,12,3,100,110,115,-64,12,120,27,-75,-82};

        //find zero byte - terminating
        //byte[] labelsBytes = Arrays.copyOfRange(bytes, 0, zeroByteIndex);

        DomainNameLabels domainNameLabels0 = new DomainNameLabels(new byte[]{4,100,110,115,49,-64,12,3,100,110,115,-64,12,120,27,-75,-82});
        DomainNameLabels domainNameLabels1 = new DomainNameLabels(new byte[]{4,100,110,115,49,0,12,3,100,110,115,-64,12,120,27,-75,-82});

        System.out.println(domainNameLabels1.labelsString);
        System.out.println(domainNameLabels1.pointerAddress);

        System.out.println(domainNameLabels0.labelsString);
        System.out.println(domainNameLabels0.pointerAddress);

    }
}
