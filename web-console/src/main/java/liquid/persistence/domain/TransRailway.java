package liquid.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 2:16 PM
 */
@Entity(name = "TRANS_RAILWAY")
public class TransRailway extends BaseTrans {
    @Transient
    private String loadingTocStr;

    /**
     * Time of Completion of Loading
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOADING_TOC")
    private Date loadingToc;

    @Transient
    private String stationToaStr;

    /**
     * Time of Arrival at station.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATION_TOA")
    private Date stationToa;

    public Date getLoadingToc() {
        return loadingToc;
    }

    public void setLoadingToc(Date loadingToc) {
        this.loadingToc = loadingToc;
    }

    public Date getStationToa() {
        return stationToa;
    }

    public void setStationToa(Date stationToa) {
        this.stationToa = stationToa;
    }

    //Transient

    public String getLoadingTocStr() {
        return loadingTocStr;
    }

    public void setLoadingTocStr(String loadingTocStr) {
        this.loadingTocStr = loadingTocStr;
    }

    public String getStationToaStr() {
        return stationToaStr;
    }

    public void setStationToaStr(String stationToaStr) {
        this.stationToaStr = stationToaStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TransRailway{");
        sb.append("loadingTocStr='").append(loadingTocStr).append('\'');
        sb.append(", loadingToc=").append(loadingToc);
        sb.append(", stationToaStr='").append(stationToaStr).append('\'');
        sb.append(", stationToa=").append(stationToa);
        sb.append('}');
        return sb.toString();
    }
}
