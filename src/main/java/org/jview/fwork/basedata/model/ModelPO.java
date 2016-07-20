package org.jview.fwork.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ai.yc.common.core.entity.BaseEntity;

@Table(name = "tf_model")
public class ModelPO extends BaseEntity {
	@Id
    @SequenceGenerator(name="cid",sequenceName="select _nextval('_tf_model_cid_seq')")
    private Long cid;
    /**
     * 方法名
     */
    @Column(name = "func_code")
    private String funcCode;

    /**
     * 类名
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 包名
     */
    @Column(name = "package_name")
    private String packageName;
    
    

    public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	/**
     * 获取方法名
     *
     * @return func_code - 方法名
     */
    public String getFuncCode() {
        return funcCode;
    }

    /**
     * 设置方法名
     *
     * @param funcCode 方法名
     */
    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    /**
     * 获取类名
     *
     * @return class_name - 类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名
     *
     * @param className 类名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取包名
     *
     * @return package_name - 包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置包名
     *
     * @param packageName 包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ModelPO other = (ModelPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getFuncCode() == null ? other.getFuncCode() == null : this.getFuncCode().equals(other.getFuncCode()))
            && (this.getClassName() == null ? other.getClassName() == null : this.getClassName().equals(other.getClassName()))
            && (this.getPackageName() == null ? other.getPackageName() == null : this.getPackageName().equals(other.getPackageName()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getFuncCode() == null) ? 0 : getFuncCode().hashCode());
        result = prime * result + ((getClassName() == null) ? 0 : getClassName().hashCode());
        result = prime * result + ((getPackageName() == null) ? 0 : getPackageName().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        return result;
    }
}