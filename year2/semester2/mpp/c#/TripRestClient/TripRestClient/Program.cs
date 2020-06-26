using System;
using System.Collections.Generic;
using System.Data;
using System.Threading.Channels;
using System.Threading.Tasks;

namespace TripRestClient
{
    class Program
    {
        static void Main(string[] args)
        {
            RunAsync().Wait();
        }

        private static async Task RunAsync()
        {
            TripClient tripClient = new TripClient("http://localhost:8080/trips/");
            await GetById(tripClient);
            await GetAll(tripClient);
            await Add(tripClient);
            await Update(tripClient);
            await Delete(tripClient);
        }

        private static async Task Delete(TripClient tripClient)
        {
            try
            {
                Console.WriteLine("Delete");
                List<Trip> trips = await tripClient.GetAll();
                Trip trip = trips[trips.Count - 1];
                Trip result = await tripClient.Delete(trip.Id);
                Console.WriteLine(result);
                await GetAll(tripClient);
            }
            catch (ServiceException se)
            {
                Console.WriteLine(se.Message);
            }
        }

        private static async Task Update(TripClient tripClient)
        {
            try
            {
                Console.WriteLine("Update");
                List<Trip> trips = await tripClient.GetAll();
                Trip trip = trips[trips.Count - 1];
                trip.Landmark = "updatedLandmark";
                Trip result = await tripClient.Update(trip);
                Console.WriteLine(result);
                await GetAll(tripClient);
            }
            catch (ServiceException se)
            {
                Console.WriteLine(se.Message);
            }
        }

        static async Task Add(TripClient tripClient)
        {
            Console.WriteLine("Add");
            Trip trip = await tripClient.Add(new Trip(-1, "merge", "a", DateTime.Now, (float) 10.0, 20));
            Console.WriteLine(trip);
            await GetAll(tripClient);
        }

        static async Task GetAll(TripClient tripClient)
        {
            Console.WriteLine("Get all");
            List<Trip> trips = await tripClient.GetAll();
            trips.ForEach(Console.WriteLine);
        }

        static async Task GetById(TripClient tripClient)
        {
            try
            {
                Console.WriteLine("Get by id");
                Trip trip = await tripClient.GetById(1);
                Console.WriteLine(trip);
            }
            catch (ServiceException se)
            {
                Console.WriteLine(se.Message);
            }
        }
    }
}