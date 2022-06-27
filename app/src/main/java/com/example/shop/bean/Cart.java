package com.example.shop.bean;


import com.example.shop.service.CartService;

public class Cart {

    private Integer id;
    private Integer count;
    private String username;
    private Integer ischecked;
    private String store;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
//        CartService.updateCount(this.count, this.id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIschecked() {
        return ischecked;
    }

    public void setIschecked(Integer ischecked) {
        this.ischecked = ischecked;
//        if (ischecked == 0){
//            CartService.updateStatus(this.id, false);
//        }else {
//           CartService.updateStatus(this.id, true);
//        }
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", count=" + count +
                ", username='" + username + '\'' +
                ", ischecked=" + ischecked +
                ", store='" + store + '\'' +
                '}';
    }
}
