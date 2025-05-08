import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    component: () => import("@/UserApp.vue"),
    children: [
      {
        path: "",
        component: () => import("@/pages/user/Home.vue"),
      },
      {
        path: "login",
        component: () => import("@/pages/user/Login.vue"),
      },
      {
        path: "cart",
        component: () => import("@/pages/user/Cart.vue"),
      },
      {
        path: "order",
        component: () => import("@/pages/user/Order.vue"),
      },
      {
        path: "orders",
        component: () => import("@/pages/user/Orders.vue"),
      },
      {
        path: "join",
        component: () => import("@/pages/user/Join.vue"),
      },
      {
        path: "mypage",
        component: () => import("@/pages/user/Mypage.vue"),
      },
      // {
      //     path: 'data',
      //     component: () => import("@/pages/user/Data.vue"),
      // },
    ],
  },
  {
    path: "/admin",
    component: () => import("@/AdminApp.vue"),
    children: [
      {
        path: "dashboard",
        component: () => import("@/pages/admin/Dashboard.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

export default router;
