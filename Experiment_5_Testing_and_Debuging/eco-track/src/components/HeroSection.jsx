import React from "react";
import SearchBar from "./SearchBar";
import { lazy, Suspense } from "react";
import PostShimmer from "./Shimmer";
import { useState } from "react";
const Posts = lazy(() => import("./posts"));


const HeroSection = () => {
  const [query, setQuery] = useState("") ;
  return (
    
      <div className="mx-auto max-w-6xl px-4 sm:px-6 md:px-10 py-16">
        
        <div className="flex flex-col items-center text-center gap-6">
          
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold text-gray-800">
            Find What You’re Looking For
          </h1>

          <p className="text-gray-600 max-w-xl">
            Search across thousands of resources in seconds. Fast, simple, and powerful.
          </p>

          <div className="w-full flex justify-center">
            <SearchBar setQuery={setQuery}/>
          </div>

        </div>
        <Suspense fallback = {<PostShimmer/>}><Posts query = {query}/></Suspense>

      </div>

  );
};

export default HeroSection;
