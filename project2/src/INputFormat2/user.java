package INputFormat2;

import java.io.DataInput; 
import java.io.DataOutput; 
import java.io.IOException; 
import org.apache.hadoop.io.WritableComparable; 

@SuppressWarnings("rawtypes")
public class user implements WritableComparable { 
private Integer age; 
private String name; 
private String sex; 
public user() {

    }

    public user(Integer age, String name, String sex) {
        this.age = age;
        this.name = name;
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User [age=" + age + ", name=" + name + ", sex=" + sex + "]";
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(age);
        out.writeUTF(name);
        out.writeUTF(sex);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.age = in.readInt();
        this.name = in.readUTF();
        this.sex = in.readUTF();
    }

    public int compareTo(user o) {
        return this.age-o.age;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((age == null) ? 0 : age.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        user other = (user) obj;
        if (age == null) {
            if (other.age != null)
                return false;
        } else if (!age.equals(other.age))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sex == null) {
            if (other.sex != null)
                return false;
        } else if (!sex.equals(other.sex))
            return false;
        return true;
    }

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}