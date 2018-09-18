package business;

import dataaccess.GenericDataAccess;

import java.util.List;

/**
 * Generic class used to represent bussiness logic for a model class.
 *
 * @see GenericDataAccess
 */
public class BusinessClass<T> {
    protected Class<T> type;
    protected GenericDataAccess<T> dataAccess;

    /**
     * @param type       class object of T
     * @param dataAccess the data access object
     */
    public BusinessClass(Class<T> type, GenericDataAccess<T> dataAccess) {
        this.type = type;
        this.dataAccess = dataAccess;
    }

    public Class<T> getType() {
        return type;
    }

    public List<T> findAll() {
        return dataAccess.findAll();
    }

    public List<T> findByField(String field, String operator, String fieldValue) {
        return dataAccess.findByField(field, operator, fieldValue);
    }

    public boolean deleteById(int id) {
        return dataAccess.deleteById(id);
    }

    public T addNew(T newItem) {
        return dataAccess.insert(newItem);
    }

    public T update(T newForm) {
        return dataAccess.update(newForm);
    }

    public T findById(int id) {
        return dataAccess.findById(id);
    }
}
