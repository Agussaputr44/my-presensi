<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Auth;
use App\Models\Attendance;
use Illuminate\Support\Facades\Log;

class UserController extends Controller
{

    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:8',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 400);
        }

        // Create the user
        $user = User::create([
            'name' => $request->name,
            'email' => $request->email,
            'password' => Hash::make($request->password),
        ]);

        // Create a token
        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'message' => 'User registered successfully.',
            'token' => $token,
            'user_data' => $user,
        ], 201);
    }


    public function login(Request $request)
    {
        // Validasi input
        $validator = Validator::make($request->all(), [
            'email' => 'required|string|email',
            'password' => 'required|string|min:8',
        ]);

        // Jika validasi gagal
        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 400);
        }

        // Mencari pengguna berdasarkan email
        $user = User::where('email', $request->email)->first();

        // Memeriksa kredensial
        if (!$user || !Hash::check($request->password, $user->password)) {
            return response()->json([
                'message' => 'Login failed, please check your credentials.',
            ], 401);
        }

        // Membuat token Sanctum
        $token = $user->createToken('auth_token')->plainTextToken;

        // Respons berhasil
        return response()->json([
            'message' => 'Login successful',
            'access_token' => $token,
            'token_type' => 'Bearer',
            'user' => $user,
        ], 200);
    }

    /**
     * Get the authenticated user's details.
     */
    public function user(Request $request)
    {
        return response()->json($request->user());
    }

    public function getHistory(Request $request)
    {
        // Check if the user is authenticated first
        if (!$request->user()) {
            Log::info('User is not authenticated');
            return response()->json([
                'message' => 'User not authenticated'
            ], 401); // 401 Unauthorized
        }

        // Get the ID of the currently authenticated user
        $userId = $request->user()->id;

        // Log the user ID to check if it is correct
        Log::info('Authenticated User ID: ' . $userId);

        // Fetch attendance records for the logged-in user
        $absensi = Attendance::where('user_id', $userId)->get();

        // Log the count of retrieved attendance records
        Log::info('Number of Attendance Records: ' . $absensi->count());

        // Check if attendance records exist
        if ($absensi->isEmpty()) {
            return response()->json([
                'message' => 'Attendance records not found for the user'
            ], 404);
        }

        return response()->json([
            'message' => 'Success',
            'data' => $absensi
        ], 200);
    }

    /**
     * Logout the user (revoke the token).
     */
    public function logout(Request $request)
    {
        try {
            $user = $request->user();

            if ($user && $user->tokens) {
                $user->tokens->each(function ($token, $key) {
                    $token->delete();
                });

                return response()->json([
                    'message' => 'Logged out successfully',
                ]);
            } else {
                return response()->json([
                    'message' => 'No tokens found for user',
                ], 404);
            }
        } catch (\Exception $e) {
            // Log exception details
            // \Log::error('Logout Error: ' . $e->getMessage());

            return response()->json([
                'message' => 'Failed to log out',
                'error' => $e->getMessage(),
            ], 500);
        }
    }
}
