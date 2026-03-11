import React from "react";
import { Link } from "react-router-dom";
import { lazy } from "react";
import dummyPosts from "../data/dummyPosts";
const Posts = ({query}) => {
    const filteredPosts = dummyPosts.filter((post) => {
       return post.title.toLowerCase().includes(query.toLowerCase())
    })
  return (
    <section className="max-w-6xl mx-auto px-4 sm:px-6 md:px-10 py-12">
      
      <h2 className="text-2xl md:text-3xl font-bold text-gray-800 mb-8">
        Latest Posts
      </h2>

      <div className="grid gap-8 sm:grid-cols-2 md:grid-cols-3">
        {filteredPosts.map((post) => (
          <Link
            key={post.id}
            to={`/posts/${post.id}`}
            className="group bg-white rounded-2xl overflow-hidden shadow-md hover:shadow-xl transition duration-300"
          >
            <div className="h-48 overflow-hidden">
              <img
                src={post.image}
                alt={post.title}
                loading="lazy"
                className="h-full w-full object-cover group-hover:scale-105 transition duration-300"
              />
            </div>
            <div className="p-4">
              <h3 className="text-lg font-semibold text-gray-800 group-hover:text-blue-600 transition">
                {post.title}
              </h3>
            </div>
          </Link>
        ))}
      </div>

    </section>
  );
};

export default Posts;
